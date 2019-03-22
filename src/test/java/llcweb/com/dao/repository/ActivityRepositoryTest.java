package llcweb.com.dao.repository;

import llcweb.com.domain.models.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityRepositoryTest {

    @Resource
    private ActivityRepository activityRepository;

    @Test
    public void getLatestTest(){
        int count=4;
        List<Activity> activities=activityRepository.getLatest(count);
        for (Activity activity:activities){
            System.out.println(activity.getTitle()+activity);
        }
    }

    @Test
    public void getActivitiesTest(){
        String type="通知";
        int count=4;
        List<Activity> activities=activityRepository.getActivities(type,count);
        for (Activity activity:activities){
            System.out.println(activity.getTitle());
        }
    }
}