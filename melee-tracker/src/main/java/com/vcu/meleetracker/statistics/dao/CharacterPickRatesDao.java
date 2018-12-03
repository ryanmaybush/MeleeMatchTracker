package com.vcu.meleetracker.statistics.dao;

import com.vcu.meleetracker.statistics.dto.CharacterPickRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.List;

@Repository
public class CharacterPickRatesDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CharacterPickRates> findAll() {
        return this.jdbcTemplate.query( "select player_id, character_id, sum(games) as games_played\n" +
                "from\n" +
                "(\n" +
                "   (select player1_id as player_id, player1_character_id as character_id, count(*) as games\n" +
                "  from matches\n" +
                "  (select player2_id as player_id, player2_character_id as character_id, count(*) as games\n" +
                "from matches\n" +
                "  group by player2_id, player2_character_id)\n" +
                "  ) all_games\n" +
                "  group by player_id, character_id;\n", new PopularThrowsMapper());
    }
    private static final class PopularThrowsMapper implements RowMapper<CharacterPickRates> {

        public CharacterPickRates mapRow(ResultSet rs, int rowNum) throws SQLException {
            CharacterPickRates characterPickRates = new CharacterPickRates();
            characterPickRates.setPlayerId(rs.getInt("player_Id"));
            characterPickRates.setCharcterId(rs.getInt("character_Id"));
            characterPickRates.setGamesPlayed(rs.getInt("games_played"));
            return characterPickRates;
        }
    }
}
