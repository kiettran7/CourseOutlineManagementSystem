package com.comwe.pojo;

import com.comwe.pojo.Class;
import com.comwe.pojo.Faculty;
import com.comwe.pojo.Student;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-05-24T22:51:46")
=======
@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-05-26T18:13:12")
>>>>>>> sendMailProfile
@StaticMetamodel(Major.class)
public class Major_ { 

    public static volatile SetAttribute<Major, Student> studentSet;
    public static volatile SingularAttribute<Major, Faculty> facultyId;
    public static volatile SingularAttribute<Major, String> name;
    public static volatile SingularAttribute<Major, Integer> id;
    public static volatile SetAttribute<Major, Class> classSet;

}