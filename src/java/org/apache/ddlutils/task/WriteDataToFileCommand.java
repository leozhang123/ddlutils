package org.apache.ddlutils.task;

/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileOutputStream;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.io.DataWriter;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Command to dump data from the database into an XML file.
 */
public class WriteDataToFileCommand extends DatabaseCommand
{
    /** The file to output the data to */
    private File   _outputFile;
    /** The character encoding to use */
    private String _encoding;

    /**
     * Sets the file to output the data to.
     * 
     * @param outputFile The output file
     */
    public void setOutputFile(File outputFile)
    {
        _outputFile = outputFile;
    }

    /**
     * Sets the output encoding.
     * 
     * @param encoding The encoding
     */
    public void setEncoding(String encoding)
    {
        _encoding = encoding;
    }

    /* (non-Javadoc)
     * @see org.apache.ddlutils.task.Command#execute(org.apache.tools.ant.Task, org.apache.ddlutils.model.Database)
     */
    public void execute(Task task, Database model) throws BuildException
    {
        try
        {
            Platform   platform = getPlatform();
            DataWriter writer   = new DataWriter(model, new FileOutputStream(_outputFile), _encoding);
            
            // TODO: An advanced algorithm could be employed here that writes objects
            //       related by foreign keys, in the correct order
            writer.writeDocumentStart();
            for (int idx = 0; idx < model.getTableCount(); idx++)
            {
                Table table = (Table)model.getTable(idx);

                writer.write(platform.query(model, "select * from "+table.getName()));
            }
            writer.writeDocumentEnd();
        }
        catch (Exception ex)
        {
            throw new BuildException(ex);
        }
    }

}