package model;

import exceptions.AddingSubjectException;
import exceptions.SubjectIllegalTitleException;

/**
 * Created by nromanen on 7/24/2017.
 */
public class Subject {

    private Long id;
    private String title;

    private Subject(String title) {
        this.title = title;
    }

    public Subject() {
    }

    public static Subject compose(String title) throws SubjectIllegalTitleException {
        if(title == null || title.contains(" ") || title.isEmpty()){
            throw new SubjectIllegalTitleException();
        }
        return new Subject(title);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Subject)){
            return false;
        }
        if(obj == this){
            return true;
        }
        return ((Subject) obj).getTitle().equals(this.getTitle());
    }

    @Override
    public int hashCode() {
        return title.hashCode() * 21 + 3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
