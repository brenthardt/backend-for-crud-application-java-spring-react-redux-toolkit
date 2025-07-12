package org.example.crud_with_redux_postgresql_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String path;
    @ManyToOne
    @JsonBackReference
    private Product product;

    public Image(String originalFilename, String path, Product product) {
        this.title = originalFilename;
        this.path = path;
        this.product = product;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }


}
