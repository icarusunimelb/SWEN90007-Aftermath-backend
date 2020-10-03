package domain;

import datamapper.ChoiceMapper;

public class Choice extends DomainObject{
    private String choice = null;
    private String questionID = null;
    public Choice(){super();};

    public String getQuestionID() {
        if(questionID == null) load();
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getChoice() {
        if(choice == null) load();
        return choice;
    }

    public void setChoice(String choice) { this.choice = choice; }

    public void load(){
        Choice record = ChoiceMapper.getSingletonInstance().findWithChoiceID(getId());
        if (this.choice == null) this.choice = record.getChoice();
        if (this.questionID == null) this.questionID = record.getQuestionID();
    }
}
