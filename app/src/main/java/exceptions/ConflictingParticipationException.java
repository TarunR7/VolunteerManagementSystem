package exceptions;

public class ConflictingParticipationException extends RuntimeException{
    @Override
    public String toString() {
        return "You have already joined an event within these timings!";
    }
}
