package kr.ac.jalee.mycypherssupporter;

public class SearchHistory {
    private int _id;
    private String nickname;

    SearchHistory(int _id, String nickname) {
        this._id = _id;
        this.nickname = nickname;
    }

    public String getNickname() { return nickname; }
}
