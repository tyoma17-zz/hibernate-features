package com.tyoma17.hibernate.second_level_cache.client;

import com.tyoma17.hibernate.second_level_cache.entity.Guide;
import com.tyoma17.hibernate.second_level_cache.util.DbUtils;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManager;

@Log4j2
public class SecondLevelCacheClient {

    public static void main(String[] args) {

        DbUtils.bootstrapDb();

        Statistics stats = DbUtils.ENTITY_MANAGER_FACTORY.unwrap(SessionFactory.class)
                .getStatistics();
        stats.setStatisticsEnabled(true);

        EntityManager em1 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Guide guide1 = em1.find(Guide.class, 2L);
        int studentsAmount = guide1.getStudents().size();
        em1.close();

        EntityManager em2 = DbUtils.ENTITY_MANAGER_FACTORY.createEntityManager();
        Guide guide2 = em2.find(Guide.class, 2L);
        // no SQL queries are executed

        int studentsAmount2 = guide2.getStudents().size();
        em2.close();

        log.info(stats.getSecondLevelCacheStatistics("com.tyoma17.hibernate.second_level_cache.entity.Guide"));
        log.info(stats.getSecondLevelCacheStatistics("com.tyoma17.hibernate.second_level_cache.entity.Student"));

//        cache invalidation
//        DbUtils.ENTITY_MANAGER_FACTORY.getCache().evict(Guide.class);
//        DbUtils.ENTITY_MANAGER_FACTORY.getCache().evict(Guide.class, 2L);
    }
}