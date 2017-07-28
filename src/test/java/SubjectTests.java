import exceptions.IllegalSubjectTitleException;
import model.Subject;
import org.junit.Test;

/**
 * Created by Aleks on 25.07.2017.
 */
public class SubjectTests {

    @Test(expected = IllegalSubjectTitleException.class)
    public void composeSubjectWithEmptyTitleThrowsException() throws IllegalSubjectTitleException {
        Subject.compose("");
    }

    @Test(expected = IllegalSubjectTitleException.class)
    public void composeSubjectWithSpacesThrowsException() throws IllegalSubjectTitleException {
        Subject.compose("Some subject");
    }

    @Test(expected = IllegalSubjectTitleException.class)
    public void composeSubjectWithNullTitleThrowsException() throws IllegalSubjectTitleException {
        Subject.compose(null);
    }
}
