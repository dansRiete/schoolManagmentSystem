package utils;

import services.BaseGradesService;
import services.GradesInMemoryService;

/**
 * Created by Aleks on 10.08.2017.
 */
public class MainService {
    public static BaseGradesService service = new GradesInMemoryService();
}
