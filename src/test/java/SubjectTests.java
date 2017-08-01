import exceptions.AddingSubjectException;
import model.Subject;
import org.junit.Test;

/**
 * Created by Aleks on 25.07.2017.
 */
public class SubjectTests {

    @Test(expected = AddingSubjectException.class)
    public void composeSubjectWithEmptyTitleThrowsException() throws AddingSubjectException {
        Subject.compose("");
    }

    @Test(expected = AddingSubjectException.class)
    public void composeSubjectWithSpacesThrowsException() throws AddingSubjectException {
        Subject.compose("Some subject");
    }

    @Test(expected = AddingSubjectException.class)
    public void composeSubjectWithNullTitleThrowsException() throws AddingSubjectException {
        Subject.compose(null);
    }
}
