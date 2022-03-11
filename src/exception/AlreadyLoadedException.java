package exception;

public class AlreadyLoadedException extends Exception{
	public AlreadyLoadedException() {
		super("This data file was already loaded");
	}
}
