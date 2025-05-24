package com.voting.votingapp.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class OptionVote {
    private String optionText;
    private Long voteCount = 0L;

    // TODO: Lombok not working in this class
    public OptionVote() {
    }

    public OptionVote(String voteOption, Long voteCount) {
        this.optionText = voteOption;
        this.voteCount = voteCount;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}
