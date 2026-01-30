package jp.co.monocrea.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jp.co.monocrea.entity.User;
import jp.co.monocrea.service.UserService;

import java.net.URI;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Response search(
            @QueryParam("id") String idParam,
            @QueryParam("name") String name
    ) {
        Long id = null;

        if (idParam != null && !idParam.isBlank()) {
            try {
                id = Long.valueOf(idParam);
            } catch (NumberFormatException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("id must be a number")
                        .build();
            }
        }

        List<User> users = userService.search(id, name);
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return userService.findById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    /**
     * 新規作成: POST /users
     * body: {"name":"..."}
     */
    @POST
    public Response create(CreateUserRequest req) {
        if (req == null || req.name == null || req.name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("name is required")
                    .build();
        }

        User created = userService.create(req.name.trim());

        // ★ created.id は可視でないので getter 経由にする
        Long createdId = created.getId();

        return Response.created(URI.create("/users/" + createdId))
                .entity(created)
                .build();
    }

    /**
     * 更新: PUT /users/{id}
     * body: {"name":"..."}
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, UpdateUserRequest req) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("id is required")
                    .build();
        }
        if (req == null || req.name == null || req.name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("name is required")
                    .build();
        }

        return userService.update(id, req.name.trim())
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    /**
     * 削除: DELETE /users/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("id is required")
                    .build();
        }

        boolean deleted = userService.delete(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("user not found")
                    .build();
        }

        return Response.noContent().build();
    }

    @OPTIONS
    @Path("{path: .*}")
    public void options() {
        // no-op
    }
}
