package org.roelee.quarkus;

import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path ("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET 
    public List<Item> getAll() {
        return Item.listAll();
    }

    @GET 
    @Path("/{id}")
    public Item getById(@PathParam("id") Long id) {
        Item item = Item.findById(id);
        if (item == null) {
            throw new NotFoundException("Item not found with id: " + id);
        }

        return item;
    }

    @POST
    @Transactional 
    public Response create(Item item) {
        item.id = null; // Ensure the ID is null so that a new item is created
        item.persist();
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @PUT 
    @Path ("/{id}")
    @Transactional
    public Item update(@PathParam("id") Long id, Item itemDetails) {
        Item existingItem = Item.findById(id);
        if (existingItem == null) {
            throw new NotFoundException("Item not found with id: " + id);
        }

        existingItem.name = itemDetails.name;
        existingItem.price = itemDetails.price;
        existingItem.persist();

        return existingItem;
    }

    @DELETE 
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Item item = Item.findById(id);
        if (item == null) {
            throw new NotFoundException("Item not found with id: " + id);
        }

        item.delete();
        return Response.noContent().build();
    }
}