package exceptions;

public class InvalidLoginException extends Exception{
    @Override
    public String toString(){
        return "Your login was Invalid!";
    }
}
