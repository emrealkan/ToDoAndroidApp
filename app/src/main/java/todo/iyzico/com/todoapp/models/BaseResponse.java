package todo.iyzico.com.todoapp.models;

/**
 * Created by emrealkan on 27/08/16.
 */
public class BaseResponse<E>
{
    private boolean success = false;
    private String message;
    private E data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
