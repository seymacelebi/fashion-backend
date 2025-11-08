package com.fashion.fashion_backend.service;

import static com.fashion.fashion_backend.entity.dto.CategoryDTOs.CategoryCreateDto;
import static com.fashion.fashion_backend.entity.dto.CategoryDTOs.CategoryDto;

import com.fashion.fashion_backend.entity.Category;
import com.fashion.fashion_backend.exception.ResourceNotFoundException; // Bu sınıfı oluşturduğunuzu varsayıyorum
import com.fashion.fashion_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryCreateDto createDto) {
        // İş Kuralı: Bu isimde bir kategori zaten var mı?
        categoryRepository.findByName(createDto.name()).ifPresent(cat -> {
            throw new IllegalArgumentException("Category with name '" + createDto.name() + "' already exists.");
        });

        Category newCategory = new Category();
        newCategory.setName(createDto.name());

        Category savedCategory = categoryRepository.save(newCategory);
        return mapToDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        // Önce kategoriyi bul
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // ÖNEMLİ İŞ KURALI: Kategoriye bağlı ürünler (products) var mı?
        // Kategori entity'nizdeki 'products' set'ini kontrol ediyoruz.
        // @Transactional sayesinde bu (lazy-loaded) set'e erişebiliriz.
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            // Eğer kategori boş değilse, silme işlemini reddet.
            throw new IllegalStateException("Category cannot be deleted: It has associated products.");
        }

        // Kategori boşsa, güvenle sil.
        categoryRepository.delete(category);
    }

    // === YARDIMCI METOT (HELPER METHOD) ===
    private CategoryDto mapToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
