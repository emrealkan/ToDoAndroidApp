package todo.iyzico.com.todoapp.models;

/**
 * Created by emrealkan on 25/08/16.
 */
public class ToDo {

    private Long todoID;
    private String title;
    private String subTitle;
    private String content;
    private String startDate;
    private String endDate;

    public Long getTodoID() {
        return todoID;
    }

    public void setTodoID(Long todoID) {
        this.todoID = todoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
