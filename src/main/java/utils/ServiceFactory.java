package utils;

import datasources.DataSource;
import services.BaseGradesService;
import services.GradesDatabaseService;
import services.GradesInMemoryService;

/**
 * Created by Aleks on 10.08.2017.
 */
public class ServiceFactory {
    private static BaseGradesService service = new GradesDatabaseService(DataSource.getSqlSessionFactory());
    public static BaseGradesService getService(){
        return service;
    }
}
