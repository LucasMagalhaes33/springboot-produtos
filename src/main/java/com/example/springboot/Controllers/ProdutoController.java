package com.example.springboot.Controllers;

import com.example.springboot.Models.ProdutoModel;
import com.example.springboot.Repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoModel>> getAllProdutos(){
        List<ProdutoModel> produtoModelList = produtoRepository.findAll();
        if (produtoModelList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            //adicionando método para cada produto ter seu proprio link salvo
        }else {
            for (ProdutoModel produto: produtoModelList){
                long id = produto.getIdProduto();
                produto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class).getOneProduto(id)).withSelfRel());
            }
            return new ResponseEntity<List<ProdutoModel>>(produtoModelList, HttpStatus.OK);
        }
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoModel> getOneProduto(@PathVariable(value = "id") long id){
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if (!produto0.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            produto0.get().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoController.class).getOneProduto(id)).withRel("lista de produtos"));
            return new ResponseEntity<ProdutoModel>(produto0.get(), HttpStatus.OK);
        }
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoModel> saveProduto(@RequestBody @Validated ProdutoModel produto){
        return new ResponseEntity<ProdutoModel>(produtoRepository.save(produto), HttpStatus.OK);
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable(value = "id") long id){
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if (!produto0.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            produtoRepository.delete(produto0.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<ProdutoModel> updateProduto(@PathVariable(value = "id") long id, @RequestBody @Validated ProdutoModel produto){
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if (!produto0.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            produto.setIdProduto(produto0.get().getIdProduto());
            return new ResponseEntity<ProdutoModel>(produtoRepository.save(produto), HttpStatus.OK);
        }
    }

}
