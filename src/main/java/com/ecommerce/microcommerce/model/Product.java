package com.ecommerce.microcommerce.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@JsonFilter("cacherChampsProduit")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(value=1, message = "L'identifiant doit forcément être strictement positif.")
    private int id;

    @NotNull(message = "Le nom du produit ne peut pas être null.")
    @Size(min=3, max=20, message="Le nom du produit doit être compris entre 3 et 20 caractères.")
    private String nom;

    @Min(value=1, message="Le prix de vente du produit doit être forcément strictement positif.")
    private int prix;

    @Min(value=1, message="Le prix d'achat du produit doit être forcément strictement positif.")
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    //@JsonProperty(value = "prixAchat", access = JsonProperty.Access.WRITE_ONLY)
    private int prixAchat;

    public Product() { }

    public Product(int id, String nom, int prix, int prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getPrixAchat() { return prixAchat; }

    public void setPrixAchat(int prixAchat) { this.prixAchat = prixAchat; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
