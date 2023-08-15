package chessGamesDatabase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "openings")
public class Opening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opening_id")
    private long openingId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "fen")
    private String fen;

    @Column(name = "pgn_moves")
    private String pgnMoves;

    @Column(name = "uci_moves")
    private String uciMoves;

    public Opening() {
    }

    public long getOpeningId() {
        return openingId;
    }

    public void setOpeningId(long openingId) {
        this.openingId = openingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getPgnMoves() {
        return pgnMoves;
    }

    public void setPgnMoves(String pgnMoves) {
        this.pgnMoves = pgnMoves;
    }

    public String getUciMoves() {
        return uciMoves;
    }

    public void setUciMoves(String uciMoves) {
        this.uciMoves = uciMoves;
    }
}
