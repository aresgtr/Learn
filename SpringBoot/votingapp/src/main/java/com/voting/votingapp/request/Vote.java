package com.voting.votingapp.request;

public class Vote {
    private Long pollId;
    private int optionIndex;

    public Vote() {
    }

    public Vote(Long pollId, int optionIndex) {
        this.pollId = pollId;
        this.optionIndex = optionIndex;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public int getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(int optionIndex) {
        this.optionIndex = optionIndex;
    }
}
