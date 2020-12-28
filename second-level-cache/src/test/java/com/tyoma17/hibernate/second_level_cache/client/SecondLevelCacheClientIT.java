package com.tyoma17.hibernate.second_level_cache.client;

import com.tyoma17.hibernate.second_level_cache.entity.Guide;
import com.tyoma17.hibernate.second_level_cache.entity.Student;
import com.tyoma17.hibernate.second_level_cache.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import net.sf.ehcache.CacheManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class SecondLevelCacheClientIT {

    @Test
    void test() {

        DbUtils.bootstrapDb();

        Statistics stats = DbUtils.ENTITY_MANAGER_FACTORY.unwrap(SessionFactory.class)
                .getStatistics();
        stats.setStatisticsEnabled(true);

        EntityManager em1 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Guide guide1 = em1.find(Guide.class, 2L);
        int studentsAmount = guide1.getStudents().size();
        em1.close();

        int guidesInCache = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("com.tyoma17.hibernate.second_level_cache.entity.Guide")
                .getSize();
        assertEquals(1, guidesInCache);

        int studentsInCache = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("com.tyoma17.hibernate.second_level_cache.entity.Student")
                .getSize();
        assertEquals(2, studentsInCache);

        DbUtils.ENTITY_MANAGER_FACTORY.getCache().evict(Guide.class);
        guidesInCache = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("com.tyoma17.hibernate.second_level_cache.entity.Guide")
                .getSize();
        assertEquals(0, guidesInCache);

        DbUtils.ENTITY_MANAGER_FACTORY.getCache().evict(Student.class);
        studentsInCache = CacheManager.ALL_CACHE_MANAGERS.get(0)
                .getCache("com.tyoma17.hibernate.second_level_cache.entity.Student")
                .getSize();
        assertEquals(0, studentsInCache);
    }
}