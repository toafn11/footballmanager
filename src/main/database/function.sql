-- tinh goal va pen
DELIMITER $
DROP FUNCTION IF EXISTS calGoalAndPen$
CREATE FUNCTION calGoalAndPen(p_matchID INT, p_teamID INT)
RETURNS INT
READS SQL DATA
BEGIN
	DECLARE score INT DEFAULT 0;
    
	SELECT COUNT(*)  INTO score
		FROM match_detail m
		JOIN players p
		ON m.player_id = p.id
		JOIN contract c
		ON p.id = c.player_id
		WHERE m.match_id = p_matchID
		AND c.team_id = p_teamID
		AND (m.moment = 'goal' OR m.moment = 'pen' )
		AND c.state = 'active';
	RETURN score;
END $

DELIMITER ;

-- tinh phan luoi
DELIMITER $
DROP FUNCTION IF EXISTS calOwnGoal$
CREATE FUNCTION calOwnGoal(p_matchID INT, p_teamID INT)
RETURNS INT
READS SQL DATA
BEGIN
	DECLARE og INT DEFAULT 0;
    
	SELECT COUNT(*)  INTO og
		FROM match_detail m
		JOIN players p
		ON m.player_id = p.id
		JOIN contract c
		ON p.id = c.player_id
		WHERE m.match_id = p_matchID
		AND c.team_id = p_teamID
		AND m.moment = 'OG'
		AND c.state = 'active';
	RETURN og;
END $

DELIMITER ;


DELIMITER $
DROP PROCEDURE IF EXISTS calMatchScore$
CREATE PROCEDURE calMatchScore(
	IN matchID INT, 
    OUT homeScore INT, 
    OUT awayScore INT)
READS SQL DATA
BEGIN
	DECLARE homeID INT;
    DECLARE awayID INT;
    
    SELECT home_id, away_id INTO homeID, awayID FROM matches WHERE id = matchID;
	SET homeScore = calGoalAndPen(matchID, homeID) + calOwnGoal(matchID, awayID);
    SET awayScore = calGoalAndPen(matchID, awayID) + calOwnGoal(matchID, homeID);
END $

DELIMITER ;

DELIMITER $


DROP PROCEDURE IF EXISTS updateRanking $
CREATE PROCEDURE updateRanking(
    IN TourID INT, 
    IN TeamID INT, 
    IN PointEarn INT, 
    IN Win INT, 
    IN Draw INT, 
    IN Loss INT, 
    IN Gf INT, 
    IN Ga INT
)
MODIFIES SQL DATA 
BEGIN
    UPDATE ranking
    SET points = points + PointEarn,
        played = played + 1,
        wins = wins + Win,
        draws = draws + Draw,
        losses = losses + Loss,
        goals = goals + Gf,
        against_goal = against_goal + Ga,
        diff = (goals + Gf) - (against_goal + Ga)
    WHERE tour_id = TourID AND team_id = TeamID;
END $

DROP PROCEDURE IF EXISTS updateMatchDone $
CREATE PROCEDURE updateMatchDone(IN matchID INT, IN homeScore INT, IN awayScore INT)
MODIFIES SQL DATA 
BEGIN
    UPDATE matches
    SET home_result = homeScore,
        away_result = awayScore,
        isDone = true
    WHERE id = matchID;
END $

DELIMITER ;


DELIMITER $
CREATE PROCEDURE finalizeMatch(matchID INT)
MODIFIES SQL DATA
BEGIN
	DECLARE homeScore INT DEFAULT 0;
    DECLARE awayScore INT DEFAULT 0;
    DECLARE homeID INT;
    DECLARE awayID INT;
    DECLARE tourID INT;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error occurred, transaction rolled back' AS Message;
    END;
    
    SELECT tour_id, home_id, away_id INTO tourID, homeID, awayID FROM matches WHERE id = matchID;
	START TRANSACTION;
		SET homeScore = calGoalAndPen(matchID, homeID) + calOwnGoal(matchID, awayID);
		SET awayScore = calGoalAndPen(matchID, awayID) + calOwnGoal(matchID, homeID);
        
		CALL updateMatchDone(matchID, homeScore, awayScore);
		IF homeScore > awayScore THEN
			CALL updateRanking(tourId, homeId, 3, 1, 0, 0, homeScore, awayScore);
			CALL updateRanking(tourId, awayId, 0, 0, 0, 1, awayScore, homeScore);
		ELSEIF homeScore < awayScore THEN
			CALL updateRanking(tourId, homeId, 0, 0, 0, 1, homeScore, awayScore);
			CALL updateRanking(tourId, awayId, 3, 1, 0, 0, awayScore, homeScore);
		ELSE
			CALL updateRanking(tourId, homeId, 1, 0, 1, 0, homeScore, awayScore);
            CALL updateRanking(tourId, awayId, 1, 0, 1, 0, awayScore, homeScore);
		END IF;
    COMMIT;
    SELECT 'Match finalized successfully' AS Message;
END $
DELIMITER ;

DELIMITER $
DROP PROCEDURE IF EXISTS handleTransfer$
CREATE PROCEDURE handleTransfer(playerID INT, toTeam INT, startDate DATE, endDate DATE, fee INT, salary INT)
MODIFIES SQL DATA
main: BEGIN
	DECLARE fromTeam INT DEFAULT NULL;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
        SELECT 'Error occurred, transaction rolled back' AS Message;
    END;
    
    SELECT team_id INTO fromTeam FROM contract WHERE player_id = playerID AND state = 'active' LIMIT 1;
    
	IF fromTeam = toTeam THEN LEAVE main;
    END IF;
    
    START TRANSACTION;
    INSERT INTO transfer(player_id, from_team, to_team, dt, fee) 
		VALUES(playerID, fromTeam, toTeam, endDate, fee);
    UPDATE contract SET state = 'expired', end_date = startDate 
		WHERE player_id = playerID AND state = 'active';
    INSERT INTO contract(player_id, team_id, start_date, end_date, salary, state) 
		VALUES(playerID, toTeam, startDate, endDate, salary,'active');
    COMMIT;
    SELECT 'Transfer successfully' AS Message;
END $
DELIMITER ;    

DELIMITER $

DROP PROCEDURE IF EXISTS deleteTournament $
CREATE PROCEDURE deleteTournament(
    IN p_tourId INT, 
    OUT p_isDeleted BOOLEAN
)
MODIFIES SQL DATA
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_isDeleted = FALSE;
    END;

    START TRANSACTION;

        DELETE FROM match_detail 
        WHERE match_id IN (SELECT id FROM matches WHERE tour_id = p_tourId);
        DELETE FROM matches WHERE tour_id = p_tourId;
        DELETE FROM ranking WHERE tour_id = p_tourId;
        DELETE FROM tournaments WHERE id = p_tourId;
        IF ROW_COUNT() > 0 THEN
            SET p_isDeleted = TRUE;
            COMMIT;
        ELSE
            SET p_isDeleted = FALSE;
            ROLLBACK;
        END IF;

END $
DELIMITER ;

DELIMITER $
DROP PROCEDURE IF EXISTS sendTransferRequest $
CREATE PROCEDURE sendTransferRequest(
    IN p_playerId INT,
    IN p_toTeamId INT,
    IN p_fee INT,
    IN p_sal INT
)
BEGIN
    DECLARE v_fromTeam INT;
    SELECT team_id INTO v_fromTeam 
    FROM contract 
    WHERE player_id = p_playerId AND state = 'active' LIMIT 1;
    INSERT INTO transfer(player_id, from_team, to_team, dt, fee, salary, state)
    VALUES (p_playerId, v_fromTeam, p_toTeamId, CURDATE(), p_fee, p_sal, 'pending');
    
END $
DELIMITER ;	



DELIMITER $

DROP PROCEDURE IF EXISTS processTransfer $

CREATE PROCEDURE processTransfer(
    IN p_transferId INT,
    IN p_decision VARCHAR(10),   
    IN p_contractEndDate DATE,   
    OUT p_message VARCHAR(255)
)
MODIFIES SQL DATA
main: BEGIN
    DECLARE v_playerId INT;
    DECLARE v_fromTeam INT;
    DECLARE v_toTeam INT;
    DECLARE v_salaryOffer INT;
    DECLARE v_currentState VARCHAR(20);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_message = 'Error with Database!';
    END;

    SELECT player_id, from_team, to_team, salary_offer, state 
    INTO v_playerId, v_fromTeam, v_toTeam, v_salaryOffer, v_currentState
    FROM transfer 
    WHERE id = p_transferId;

    -- Validation
    IF v_playerId IS NULL THEN
        SET p_message = 'Invalid player!';
        LEAVE main;
    END IF;

    IF v_currentState != 'pending' THEN
        SET p_message = 'Not pending!';
        LEAVE main;
    END IF;

    START TRANSACTION;

    IF p_decision = 'rejected' THEN
        UPDATE transfer SET state = 'rejected' WHERE id = p_transferId;
        SET p_message = 'Rejected';
        
    ELSEIF p_decision = 'accepted' THEN
        UPDATE transfer SET state = 'accepted' WHERE id = p_transferId;

        IF v_fromTeam IS NOT NULL THEN
            UPDATE contract 
            SET state = 'expired', end_date = CURDATE() 
            WHERE player_id = v_playerId AND state = 'active';
        END IF;

        INSERT INTO contract(player_id, team_id, start_date, end_date, salary, state) 
        VALUES(v_playerId, v_toTeam, CURDATE(), p_contractEndDate, v_salaryOffer, 'active');

        SET p_message = 'Accepted!';
    ELSE
        SET p_message = 'Invalid state!';
        ROLLBACK;
        LEAVE main;
    END IF;

    COMMIT;

END $
DELIMITER ;