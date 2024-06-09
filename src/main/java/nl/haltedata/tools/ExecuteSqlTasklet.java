package nl.haltedata.tools;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManagerFactory;

public class ExecuteSqlTasklet implements Tasklet {
    private EntityManagerFactory entityManagerFactory;
    private TransactionTemplate transactionTemplate;
    private String sqlStatement;
    
    public ExecuteSqlTasklet(EntityManagerFactory entityManagerFactory, TransactionTemplate transactionTemplate,
            String sqlStatement) {
        super();
        this.entityManagerFactory = entityManagerFactory;
        this.transactionTemplate = transactionTemplate;
        this.sqlStatement = sqlStatement;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (
                var entityManager = entityManagerFactory.createEntityManager();
        )
        {
            transactionTemplate.execute(transactionStatus -> {
                entityManager.joinTransaction();
                entityManager
                  .createNativeQuery(sqlStatement)
                  .executeUpdate();
                transactionStatus.flush();
                return RepeatStatus.FINISHED;
            });
        }
        finally {
            //
        }
        return RepeatStatus.FINISHED;
    }

}
