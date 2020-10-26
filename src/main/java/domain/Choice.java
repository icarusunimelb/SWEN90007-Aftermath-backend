package domain;

import datamapper.ChoiceMapper;
import exceptions.RecordNotExistException;

public class Choice extends DomainObject implements Comparable{
    private String choice = null;
    private String questionID = null;



    private int index = -1;
    public Choice(){super();};

    public String getQuestionID() throws RecordNotExistException{
        if(questionID == null) load();
        return questionID;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getChoice() throws RecordNotExistException{
        if(choice == null) load();
        return choice;
    }

    public void setChoice(String choice) { this.choice = choice; }

    public void load() throws RecordNotExistException{
        Choice record = ChoiceMapper.getSingletonInstance().findWithChoiceID(getId());
        if (record.getId() == null) {
            throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
        }
        if (this.choice == null) this.choice = record.getChoice();
        if (this.questionID == null) this.questionID = record.getQuestionID();
        if (this.index == -1) this.index = record.getIndex();
    }

    @Override
    public int compareTo(Object o) {
        int compareage=((Choice) this).getIndex();
        /* For Ascending order*/
        return this.index-compareage;

    }
}
