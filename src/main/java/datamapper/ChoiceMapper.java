package datamapper;

import datasource.DBConnection;
import domain.Choice;
import domain.DomainObject;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoiceMapper extends DataMapper{
    private static volatile ChoiceMapper instance = null;

    public static ChoiceMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (ChoiceMapper.class) {
                if (instance == null) {
                    instance = new ChoiceMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithChoiceIDStatement = "SELECT c.choice, c.questionID, c.index"
            + "FROM oes.choices c "
            + "WHERE c.choiceID = ?";

    public Choice findWithChoiceID (String choiceID) {
        Choice choice = new Choice();

        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithChoiceIDStatement);
            findStatement.setString(1, choiceID);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String choiceContent = rs.getString(1);
                String questionID = rs.getString(2);
                int index = rs.getInt(3);
                choice.setId(choiceID);
                choice.setQuestionID(questionID);
                choice.setChoice(choiceContent);
                choice.setIndex(index);
                IdentityMap.getInstance(choice).put(choiceID, choice);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return choice;
    }

    private static final String findWithQuestionIDStatement = "SELECT c.choiceID, c.choice, c.index "
            + "FROM oes.choices c "
            + "WHERE c.questionID = ?";
    public List<Choice> findWithQuestionID (String questionID) {
        List<Choice> choices = new ArrayList<Choice>();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithQuestionIDStatement);
            findStatement.setString(1,questionID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                Choice choice = new Choice();
                choice.setQuestionID(questionID);
                String choiceID = rs.getString(1);
                choice.setId(choiceID);
                String choiceContent = rs.getString(2);
                choice.setChoice(choiceContent);
                int index = rs.getInt(3);
                choice.setIndex(index);
                IdentityMap.getInstance(choice).put(choiceID, choice);
                choices.add(choice);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        Collections.sort(choices);
        return choices;
    }


    private static final String insertChoiceStatement =
            "INSERT INTO oes.choices (choiceID, questionID, choice, index) VALUES (?, ?, ?, ?)";
    @Override
    public void insert(DomainObject object) {
        Choice choiceObj = (Choice) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertChoiceStatement);
            insertStatement.setString(1, choiceObj.getId());
            insertStatement.setString(2, choiceObj.getQuestionID());
            insertStatement.setString(3, choiceObj.getChoice());
            insertStatement.setInt(4,choiceObj.getIndex());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static final String updateChoiceStatement =
            "UPDATE oes.choices c SET c.choice = ? WHERE c.choiceID = ?";
    @Override
    public void update(DomainObject object) {
        Choice choiceObj = (Choice) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateChoiceStatement);
            updateStatement.setString(1, choiceObj.getChoice());
            updateStatement.setString(2, choiceObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String deleteChoiceStatement =
            "DELETE FROM oes.choices c WHERE c.choiceID = ?";
    @Override
    public void delete(DomainObject object) {
        Choice choiceObj = (Choice) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteChoiceStatement);
            updateStatement.setString(1, choiceObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
