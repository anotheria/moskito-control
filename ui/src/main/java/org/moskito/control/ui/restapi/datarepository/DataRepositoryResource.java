package org.moskito.control.ui.restapi.datarepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import net.anotheria.util.StringUtils;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.config.datarepository.RetrieverInstanceConfig;
import org.moskito.control.config.datarepository.VariableMapping;
import org.moskito.control.data.DataRepository;
import org.moskito.control.ui.restapi.ReplyObject;
import org.moskito.control.ui.restapi.control.ThresholdBean;

import java.util.*;

@Path("datarepository")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
@Tag(name = "Data Repository API", description = "API for data repository access.")
/**
 * This class is responsible for handling methods for datarepository inspection.
 */

public class DataRepositoryResource {
    @Path("store")
    @GET
    @Operation(summary = "Returns all formulas and values for the connected component",
            description = "Returns all calculated formulas, retrievable value and current value of each entity."
    )
    @ApiResponse(description = "Data Entities as list named 'data'",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DataEntityBean.class))
            ))

    public ReplyObject getStore(){
        MoskitoControlConfiguration config = MoskitoControlConfiguration.getConfiguration();
        HashMap<String, DataEntityBean> beans = new HashMap<>();

        //Set formulas for beans.
        for (String processingLine : config.getDataprocessing().getProcessing()) {
            String tokens[] = StringUtils.tokenize(processingLine, ' ');
            if (tokens.length > 1) {
                String variableName = tokens[1];
                DataEntityBean bean = new DataEntityBean();
                bean.setName(variableName);
                if (!beans.containsKey(variableName)) {
                    beans.put(variableName, bean);
                }
                beans.get(variableName).addFormula(processingLine);
            }
        }

        Map<String, String> retrieverVariableNames = new HashMap<>();

        for (RetrieverInstanceConfig retriever : config.getDataprocessing().getRetrievers()) {
            for (VariableMapping mapping : retriever.getMappings()) {
                retrieverVariableNames.put(mapping.getVariableName(), mapping.getExpression());
            }
        }

        //Set directly set values.
        for (String key : DataRepository.getInstance().getData().keySet()) {
            if (!beans.containsKey(key)) {
                DataEntityBean bean = new DataEntityBean();
                bean.setName(key);
                bean.addFormula(retrieverVariableNames.get(key));
            }
        }

        //Now set all the values for prepared beans.
        DataRepository.getInstance().getData().forEach((key, value) -> {
            if (beans.containsKey(key)) {
                beans.get(key).setValue(value);
            }
        });

        return ReplyObject.success("data", beans.values()   );

    }
}
