package webprog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryProductApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body").isArray());
    }

    @Test
    void testGetCategoryById() throws Exception {
        mockMvc.perform(post("/api/category/getCategory").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.categoryId").value(1));
    }

    @Test
    void testAddCategoryWithIcon() throws Exception {
        MockMultipartFile file = new MockMultipartFile("icon", "test-icon.png", "image/png", "dummy-image-content".getBytes());
        mockMvc.perform(multipart("/api/category/addCategory")
                        .file(file)
                        .param("categoryName", "Danh Mục Test API"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.categoryName").value("Danh Mục Test API"));
    }

    @Test
    void testPaginatedCategories() throws Exception {
        mockMvc.perform(get("/api/category/paginated")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.content").isArray());
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body").isArray());
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(post("/api/product/getProduct").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.productId").value(1));
    }

    @Test
    void testAddProductWithImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "test-product.png", "image/png", "dummy-image".getBytes());
        mockMvc.perform(multipart("/api/product/addProduct")
                        .file(file)
                        .param("productName", "Sản Phẩm Test MockMvc")
                        .param("unitPrice", "199000")
                        .param("discount", "10")
                        .param("quantity", "20")
                        .param("description", "Mô tả sản phẩm test")
                        .param("categoryId", "1")
                        .param("status", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.productName").value("Sản Phẩm Test MockMvc"));
    }

    @Test
    void testPaginatedProducts() throws Exception {
        mockMvc.perform(get("/api/product/paginated")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.body.content").isArray());
    }
}

