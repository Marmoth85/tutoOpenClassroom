package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDAO;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Product API", description = "API de gestion de produits")
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    @RequestMapping(value="/Produits", method= RequestMethod.GET)
    @Operation(summary="${getallproducts.summary}", description = "${getallproducts.description}")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "${getallproducts.response200}",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))}
            ),
            //@ApiResponse(responseCode = "400", description = "${getallproducts.response400}", content={@Content} ),
            @ApiResponse(responseCode = "404", description = "${getallproducts.response404}", content={@Content} )
    })
    public MappingJacksonValue listeProduits() {

        List<Product> products = productDAO.findAll();

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listeFiltres = new SimpleFilterProvider().addFilter("cacherChampsProduit", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(products);
        produitsFiltres.setFilters(listeFiltres);

        return produitsFiltres;
    }


    @GetMapping(value="/Produits/{id}")
    @Operation(summary="${getproduct.summary}", description = "${getproduct.description}")
    @Parameters(value = {
            @Parameter(name = "id", in = ParameterIn.PATH, description = "${product.id}", required = true)
    })
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "${getproduct.response200}",
                    content={@Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "${getproduct.response400}" /*, content={@Content}*/ ),
            @ApiResponse(responseCode = "404", description = "${getproduct.response404}", content={@Content} )
    })
    public MappingJacksonValue afficherUnProduit(@PathVariable @Min(value=1, message = "L'identifiant du produit ne peut pas être inférieur à 1") int id){

        Product product = productDAO.findById(id);

        if (product == null) throw new ProductNotFoundException("Le produit dont l'identifiant est " + id + " est introuvable");

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listeFiltre = new SimpleFilterProvider().addFilter("cacherChampsProduit", monFiltre);
        MappingJacksonValue produitFiltre = new MappingJacksonValue(product);
        produitFiltre.setFilters(listeFiltre);

        return produitFiltre;
    }


    @PostMapping(value="/Produits")
    @Operation(summary = "${postproduct.summary}", description = "${postproduct.description}")
    @Parameters(value = {
            @Parameter(name = "product", in = ParameterIn.PATH, description = "${product.parameter}", required = true, schema = @Schema(implementation = Product.class) )
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${postproduct.response201}", content = @Content),
            @ApiResponse(responseCode = "204", description = "${postproduct.response204}", content = @Content),
            @ApiResponse(responseCode = "400", description = "${postproduct.response400}" /*, content = @Content*/)
    })
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded = productDAO.save(product);

        if (productAdded == null) return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value="/Produits/{id}")
    @Operation(summary = "${deleteproduct.summary}", description = "${deleteproduct.description}")
    @Parameters(value = {
            @Parameter(name = "id", in = ParameterIn.PATH, description = "${product.id}", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${deleteproduct.response200}", content = @Content),
            @ApiResponse(responseCode = "202", description = "${deleteproduct.response202}", content = @Content),
            @ApiResponse(responseCode = "204", description = "${deleteproduct.response204}", content = @Content),
            @ApiResponse(responseCode = "400", description = "${deleteproduct.response400}" /*, content = @Content*/)
    })
    public void supprimerProduit(@PathVariable @Min(value=1, message = "L'identifiant du produit ne peut pas être inférieur à 1") int id) {

        Product product = productDAO.findById(id);
        if (product != null) productDAO.delete(product);
    }

    @PutMapping(value="/Produits")
    @Operation(summary = "${putproduct.summary}", description = "${putproduct.description}")
    @Parameters(value = {
            @Parameter(name = "product", in = ParameterIn.PATH, description = "${product.parameter}", required = true, schema = @Schema(implementation = Product.class) )
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${putproduct.response201}", content = @Content),
            @ApiResponse(responseCode = "204", description = "${putproduct.response204}", content = @Content),
            @ApiResponse(responseCode = "400", description = "${putproduct.response400}" /*, content = @Content*/)
    })
    public void updateProduit(@Valid @RequestBody Product product) {
        productDAO.save(product);
    }


    @GetMapping(value = "/AdminProduits")
    @Operation(summary = "${getmarginproduct.summary}", description = "${getmarginproduct.description}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${getmarginproduct.response200}"),
            @ApiResponse(responseCode = "404", description = "${getmarginproduct.response404}", content = {@Content})
    })
    public Map<Product, Integer> calculerMargeProduit(){

        Map<Product, Integer> results = new HashMap<>();
        List<Product> products = productDAO.findAll();
        products.forEach(product -> results.put(product, product.getPrix() - product.getPrixAchat()));
        return results;
    }

    @GetMapping(value = "/TriProduits")
    @Operation(summary = "${triproductnom.summary}", description = "${triproductnom.description}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${triproductnom.response200}",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(accessMode = Schema.AccessMode.READ_WRITE, implementation = Product.class)))
            }),
            @ApiResponse(responseCode = "404", description = "${triproductnom.response404}", content = {@Content})
    })
    public MappingJacksonValue trierProduitParOrdreAlphabetique() {

        SimpleBeanPropertyFilter noFilter = SimpleBeanPropertyFilter.serializeAll();
        FilterProvider noFilterProvider = new SimpleFilterProvider().addFilter("cacherChampsProduit", noFilter);
        MappingJacksonValue results = new MappingJacksonValue(productDAO.findAllByOrderByNomAsc());
        results.setFilters(noFilterProvider);

        return results;
    }
}
