package org.example;

public class FootballEvent {
    public String getId_odsp() {
        return id_odsp;
    }

    public void setId_odsp(String id_odsp) {
        this.id_odsp = id_odsp;
    }

    private String id_odsp;

    private String id_event;
    public String getId_event() {
        return id_event;
    }
    public void setId_event(String id_event) {
        this.id_event = id_event;
    }

//
//    public int getSort_order() {
//        return sort_order;
//    }
//
//    public void setSort_order(int sort_order) {
//        this.sort_order = sort_order;
//    }

    private int sort_order;

    public int getGame_minute() {
        return game_minute;
    }

    public void setGame_minute(int game_minute) {
        this.game_minute = game_minute;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }

    public String getEvent_type2() {
        return event_type2;
    }

    public void setEvent_type2(String event_type2) {
        this.event_type2 = event_type2;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String getEvent_team() {
        return event_team;
    }

    public void setEvent_team(String event_team) {
        this.event_team = event_team;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer_in() {
        return player_in;
    }

    public void setPlayer_in(String player_in) {
        this.player_in = player_in;
    }

    public String getPlayer_out() {
        return player_out;
    }

    public void setPlayer_out(String player_out) {
        this.player_out = player_out;
    }

    public String getShot_place() {
        return shot_place;
    }

    public void setShot_place(String shot_place) {
        this.shot_place = shot_place;
    }

    public String getShot_outcome() {
        return shot_outcome;
    }

    public void setShot_outcome(String shot_outcome) {
        this.shot_outcome = shot_outcome;
    }

    public int getIs_goal() {
        return is_goal;
    }

    public void setIs_goal(int is_goal) {
        this.is_goal = is_goal;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public int getAssist_method() {
        return assist_method;
    }

    public void setAssist_method(int assist_method) {
        this.assist_method = assist_method;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public int getFast_break() {
        return fast_break;
    }

    public void setFast_break(int fast_break) {
        this.fast_break = fast_break;
    }

    private int game_minute;
    private String text;
    private int event_type;
    private String event_type2;
    private int side;
    private String event_team;
    private String opponent;
    private String player;
    private String player2;
    private String player_in;
    private String player_out;
    private String shot_place;
    private String shot_outcome;
    private int is_goal;
    private String location;
    private String bodypart;
    private int assist_method;
    private String situation;
    private int fast_break;

    // Constructor, getters, and setters for all fields
    public FootballEvent(String id_odsp, String id_event, int sort_order, int game_minute, String text, int event_type, String event_type2, int side, String event_team, String opponent, String player, String player2, String player_in, String player_out, String shot_place, String shot_outcome, int is_goal, String location, String bodypart, int assist_method, String situation, int fast_break) {
        this.id_odsp = id_odsp;
        this.id_event = id_event;
        this.sort_order = sort_order;
        this.game_minute = game_minute;
        this.text = text;
        this.event_type = event_type;
        this.event_type2 = event_type2;
        this.side = side;
        this.event_team = event_team;
        this.opponent = opponent;
        this.player = player;
        this.player2 = player2;
        this.player_in = player_in;
        this.player_out = player_out;
        this.shot_place = shot_place;
        this.shot_outcome = shot_outcome;
        this.is_goal = is_goal;
        this.location = location;
        this.bodypart = bodypart;
        this.assist_method = assist_method;
        this.situation = situation;
        this.fast_break = fast_break;
    }

    // Getters and setters for all fields
    // ...

    // You might also want to override the toString() method for debugging purposes
    @Override
    public String toString() {
        return "FootballEvent{" +
                "id_odsp='" + id_odsp + '\'' +
                ", id_event='" + id_event + '\'' +
                ", sort_order=" + sort_order +
                ", game_minute=" + game_minute +
                ", text='" + text + '\'' +
                ", event_type=" + event_type +
                ", event_type2='" + event_type2 + '\'' +
                ", side=" + side +
                ", event_team='" + event_team + '\'' +
                ", opponent='" + opponent + '\'' +
                ", player='" + player + '\'' +
                ", player2='" + player2 + '\'' +
                ", player_in='" + player_in + '\'' +
                ", player_out='" + player_out + '\'' +
                ", shot_place='" + shot_place + '\'' +
                ", shot_outcome='" + shot_outcome + '\'' +
                ", is_goal=" + is_goal +
                ", location='" + location + '\'' +
                ", bodypart='" + bodypart + '\'' +
                ", assist_method=" + assist_method +
                ", situation='" + situation + '\'' +
                ", fast_break=" + fast_break +
                '}';
    }
}
