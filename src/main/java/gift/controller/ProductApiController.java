package gift.controller;

import gift.domain.model.dto.ProductAddRequestDto;
import gift.domain.model.dto.ProductResponseDto;
import gift.domain.model.dto.ProductUpdateRequestDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Validated
@Tag(name = "Product", description = "상품 관리 API")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 조회", description = "지정된 ID의 상품을 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 페이지네이션하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
        @RequestParam(defaultValue = "10") @Positive int size,
        @RequestParam(defaultValue = "name,asc") String sort,
        @RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.getAllProducts(page, size, sort, categoryId));
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(
        @Valid @RequestBody ProductAddRequestDto productAddRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.addProduct(productAddRequestDto));
    }

    @Operation(summary = "상품 수정", description = "지정된 ID의 상품을 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequestDto productUpdateRequestDto) {
        return ResponseEntity.ok(productService.updateProduct(productId, productUpdateRequestDto));
    }

    @Operation(summary = "상품 삭제", description = "지정된 ID의 상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
