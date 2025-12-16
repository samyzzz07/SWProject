package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.util.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 * Service class for scoring-related operations.
 * Implements PostScores, RecordMatchResults, UpdateGame, and UpdateScore use cases.
 */
public class ScoringService {
    
    /**
     * Posts scores for a match.
     * Implements PostScores use case.
     * 
     * @param matchId the match ID
     * @param team1Score the score for team 1
     * @param team2Score the score for team 2
     * @return true if successful, false otherwise
     */
    public boolean postScores(Long matchId, Integer team1Score, Integer team2Score) {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            em.getTransaction().begin();
            
            Match match = em.find(Match.class, matchId);
            if (match == null) {
                em.getTransaction().rollback();
                return false;
            }
            
            // Update scores (implements UpdateScore)
            match.setTeam1Score(team1Score);
            match.setTeam2Score(team2Score);
            
            // Update game status (implements UpdateGame)
            match.setStatus(Match.MatchStatus.COMPLETED);
            
            em.merge(match);
            em.getTransaction().commit();
            return true;
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
            
        } finally {
            em.close();
        }
    }
    
    /**
     * Records match results.
     * Implements RecordMatchResults use case.
     * This includes both UpdateGame and UpdateScore.
     * 
     * @param matchId the match ID
     * @param team1Score the score for team 1
     * @param team2Score the score for team 2
     * @return true if successful, false otherwise
     */
    public boolean recordMatchResults(Long matchId, Integer team1Score, Integer team2Score) {
        return postScores(matchId, team1Score, team2Score);
    }
}
