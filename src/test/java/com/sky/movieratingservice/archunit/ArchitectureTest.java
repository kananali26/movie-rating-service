package com.sky.movieratingservice.archunit;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;

@AnalyzeClasses(packages = "com.sky.movieratingservice",
        importOptions = {ImportOption.DoNotIncludeTests.class,
                ImportOption.DoNotIncludeJars.class,
                ImportOption.DoNotIncludeArchives.class,
                ImportOption.DoNotIncludePackageInfos.class})
public class ArchitectureTest {

    @DisplayName("Inner layer classes should not depend on outer layer classes")
    @ArchTest
    void innerLayersShouldNotDependOnOuterLayers(JavaClasses javaClasses) {
        boolean hasUseCaseLayer = javaClasses.stream()
                .anyMatch(javaClass -> javaClass.getPackageName().contains("usecases"));

        boolean hasDomainLayer = javaClasses.stream()
                .anyMatch(javaClass -> javaClass.getPackageName().contains("domain"));

        boolean hasInterfaceLayer = javaClasses.stream()
                .anyMatch(javaClass -> javaClass.getPackageName().contains("interfaces")
                        || javaClass.getPackageName().contains("configuration"));

        if (hasUseCaseLayer && hasDomainLayer && hasInterfaceLayer) {
            ArchRule rule = layeredArchitecture()
                    .consideringAllDependencies()
                    .layer("Interface").definedBy("..interfaces..", "..configuration..")
                    .layer("UseCase").definedBy("..usecases..")
                    .layer("Domain").definedBy("..domain..")
                    .whereLayer("Interface").mayNotBeAccessedByAnyLayer()
                    .whereLayer("UseCase").mayOnlyBeAccessedByLayers("Interface")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Interface", "UseCase", "Interface");

            rule.check(javaClasses);
        }
    }
}
