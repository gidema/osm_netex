package nl.haltedata.osm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;

import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.xml.v0_6.XmlWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.WritableResource;
import org.springframework.util.Assert;

public class OsmNodeItemWriter<T> extends AbstractItemStreamItemWriter<T>
    implements ResourceAwareItemWriterItemStream<T>, InitializingBean {

        private OsmNodeFactory<T> nodeFactory;
        private boolean upload = true;
        private final Date timestamp;
        private WritableResource resource;
        protected OutputState state = null;

        public OsmNodeItemWriter(OsmNodeFactory<T> nodeFactory, boolean upload) {
            super();
            this.upload = upload;
            this.timestamp = new java.sql.Date(System.currentTimeMillis());
            this.nodeFactory = nodeFactory;
        }

        /**
         * Initialize the reader. This method may be called multiple times before close is
         * called.
         *
         * @see ItemStream#open(ExecutionContext)
         */
        @Override
        public void open(ExecutionContext executionContext) throws ItemStreamException {

            Assert.notNull(resource, "The resource must be set");

            if (!getOutputState().isInitialized()) {
                doOpen(executionContext);
            }
        }
        
        private void doOpen(ExecutionContext executionContext) throws ItemStreamException {
            OutputState outputState = getOutputState();
            try {
                executionContext.putString("upload", upload ? "true" : "false");
                outputState.initializeBufferedWriter();
            }
            catch (IOException ioe) {
                throw new ItemStreamException("Failed to initialize writer", ioe);
            }
        }

        @Override
        public void write(Chunk<? extends T> items) throws Exception {
            OutputState outputState = getOutputState();
            try {
                for (T item : items) {
                    var node = nodeFactory.create(item, timestamp);
                    outputState.write(node);
                }
            } catch (IOException e) {
                throw e;
            }
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setResource(WritableResource resource) {
            this.resource = resource;
        }
        
        
        @Override
        public void close() {
            OutputState outputState = getOutputState();
            if (outputState != null) outputState.close();
        }

        // Returns object representing state.
        protected OutputState getOutputState() {
            if (state == null) {
                File file;
                try {
                    file = resource.getFile();
                }
                catch (IOException e) {
                    throw new ItemStreamException("Could not convert resource to file: [" + resource + "]", e);
                }
                Assert.state(!file.exists() || file.canWrite(), "Resource is not writable: [" + resource + "]");
                state = new OutputState();
            }
            return state;
        }

        /**
         * Encapsulates the runtime state of the writer. All state changing operations on the
         * writer go through this class.
         */
        protected class OutputState {

            private FileWriter ow;

            // The bufferedWriter over the file channel that is actually written
            BufferedWriter outputBufferedWriter;
            
            XmlWriter xmlWriter; 

            // this represents the charset encoding (if any is needed) for the
            // output file
            String encoding = "UTF-8";

            boolean restarted = false;

            long linesWritten = 0;

            boolean shouldDeleteIfExists = true;

            boolean initialized = false;

            public long getLinesWritten() {
                return linesWritten;
            }

            public void setLinesWritten(long linesWritten) {
                this.linesWritten = linesWritten;
            }

            /**
             * Close the open resource and reset counters.
             */
            public void close() {

                initialized = false;
                restarted = false;
                try {
                    if (xmlWriter != null) {
                        xmlWriter.complete();
                        xmlWriter.close();
                    }
                    if (outputBufferedWriter != null) {
                        outputBufferedWriter.close();
                    }
                }
                catch (IOException ioe) {
                    throw new ItemStreamException("Unable to close the ItemWriter", ioe);
                }
                finally {
                    closeStream();
                }
            }

            private void closeStream() {
                try {
                    if (ow != null) {
                        ow.close();
                    }
                }
                catch (IOException ioe) {
                    throw new ItemStreamException("Unable to close the ItemWriter", ioe);
                }
            }

            /**
             * @param node Node to be written to the file
             * @throws IOException If unable to write the Node to the file
             */
            public void write(Node node) throws IOException {
                if (!initialized) {
                    initializeBufferedWriter();
                }
                xmlWriter.process(new NodeContainer(node));
            }

            /**
             * Creates the buffered writer for the output file channel based on configuration
             * information.
             * @throws IOException if unable to initialize buffer
             */
            private void initializeBufferedWriter() throws IOException {

                File file = resource.getFile();
                FileUtils.setUpOutputFile(file, restarted, false, true);

                ow = new FileWriter(file.getAbsolutePath(), false);

                outputBufferedWriter = new BufferedWriter(ow);
                outputBufferedWriter.flush();
                xmlWriter = new XmlWriter(outputBufferedWriter);

                Assert.state(outputBufferedWriter != null, "Unable to initialize buffered writer");

                initialized = true;
            }

            public boolean isInitialized() {
                return initialized;
            }
        }       
    }