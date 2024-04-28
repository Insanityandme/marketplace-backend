package org.example.marketplacebackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.marketplacebackend.DTO.incoming.ProductDTO;
import org.example.marketplacebackend.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProductEndpoints {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ProductRepository productRepository;

  @BeforeEach
  public void setup() {
    // init MockMvc Object and build
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  @Transactional
  @WithMockUser
  void uploadProduct() throws Exception {
    UUID productType = UUID.fromString("d5509745-450f-4760-8bdd-ddc88d376b37");
    UUID seller = UUID.fromString("dc254b85-6610-43c9-9f48-77a80b798158");
    ProductDTO product = new ProductDTO("test", productType,
        500, 0, "wow amazing", seller,
        null, null);

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(product);
    Resource file = new ClassPathResource("test_data/test.jpg");
    Resource file2 = new ClassPathResource("test_data/test2.jpg");

    MockMultipartFile jsonProduct = new MockMultipartFile("json", "", "application/json",
        json.getBytes());
    MockMultipartFile imageFile = new MockMultipartFile("data", file.getFilename(),
        "multipart/form-data", file.getContentAsByteArray());
    MockMultipartFile imageFile2 = new MockMultipartFile("data", file2.getFilename(),
        "multipart/form-data", file2.getContentAsByteArray());

    ResultActions createUser = mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/products")
        .file(jsonProduct)
        .file(imageFile)
        .file(imageFile2));

    createUser.andExpect(status().isCreated());
  }

  @AfterEach
  void tearDown() {
    // NOTE: Remove this to check the actual values in the DB
    // Remove product and also its product image
    productRepository.deleteByName("test");
  }
}
