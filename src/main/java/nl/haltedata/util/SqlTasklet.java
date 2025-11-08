package nl.haltedata.util;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public class SqlTasklet implements Tasklet {
    @Inject EntityManager entityManager;
    @Inject PlatformTransactionManager transactionManager;
    
    private final String query;
    private TransactionTemplate transactionTemplate;
    
    public SqlTasklet(String query) {
        super();
        this.query = query;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            transactionTemplate.execute(transactionStatus -> {
                entityManager.joinTransaction();
                entityManager
                  .createNativeQuery(query)
                  .executeUpdate();
                transactionStatus.flush();
                return null;
            });
        return null;
    }
}
