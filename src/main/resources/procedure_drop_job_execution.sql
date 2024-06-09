CREATE OR REPLACE PROCEDURE drop_job_execution(id int)
language plpgsql
as $$
BEGIN
  DELETE FROM batch_step_execution_context
  WHERE step_execution_id IN (SELECT step_execution_id FROM batch_step_execution WHERE job_execution_id = id);
  DELETE FROM batch_step_execution WHERE job_execution_id = id;
  DELETE FROM batch_job_execution_params WHERE job_execution_id = id;
  DELETE FROM batch_job_execution_context WHERE job_execution_id = id;
  DELETE FROM batch_job_execution WHERE job_execution_id = id;

END;$$;