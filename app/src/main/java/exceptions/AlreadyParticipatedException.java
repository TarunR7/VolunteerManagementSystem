package exceptions;

public class AlreadyParticipatedException extends RuntimeException  {
    @Override
    public String toString() {
        return "You have already joined this event!";
    }
}
