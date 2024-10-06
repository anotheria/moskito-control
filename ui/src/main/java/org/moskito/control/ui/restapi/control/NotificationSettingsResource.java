package org.moskito.control.ui.restapi.control;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import net.anotheria.util.TimeUnit;
import org.moskito.control.core.MuteStatus;
import org.moskito.control.core.Repository;
import org.moskito.control.ui.restapi.ReplyObject;

@Path("notificationSettings")
@Produces(MediaType.APPLICATION_JSON)
@Server(url = "/api/v2")
@Tag(name = "Notification Settings API", description = "API for notification settings")
public class NotificationSettingsResource {
    @Operation(summary = "Returns mute status",
            description = "Returns whether the system is muted currently and for how long"
    )
    @ApiResponse(description = "Current mute status",
            content = @Content(schema = @Schema(implementation = MuteStatus.class)))
    @GET
    @Path("status")
    public ReplyObject getMuteStatus(){
        return new ReplyObject("muteStatus", Repository.getInstance().getEventsDispatcher().getMuteStatus());
    }

    @Operation(summary = "Mutes the system",
            description = "Mutes all notifications for the provided number of minutes, or default if not provided. Used for planed maintenance windows"
    )
    @ApiResponse(description = "Current mute status",
            content = @Content(schema = @Schema(implementation = MuteStatus.class)))
    @POST @Path("mute")
    public ReplyObject mute(@QueryParam("minutes") Integer minutes){
        if (minutes==null)
            Repository.getInstance().getEventsDispatcher().mute();
        else
            Repository.getInstance().getEventsDispatcher().mute(TimeUnit.MINUTE.getMillis(minutes));
        return new ReplyObject("muteStatus", Repository.getInstance().getEventsDispatcher().getMuteStatus());
    }

    @Operation(summary = "Unmutes the system",
            description = "Cancels previously set mute status. This is useful if you muted for a maintenace window and want to unmute after it is over"
    )
    @ApiResponse(description = "Current mute status",
            content = @Content(schema = @Schema(implementation = MuteStatus.class)))
    @POST @Path("unmute")
    public ReplyObject unmute(){
        Repository.getInstance().getEventsDispatcher().unmute();
        return new ReplyObject("muteStatus", Repository.getInstance().getEventsDispatcher().getMuteStatus());
    }
}
