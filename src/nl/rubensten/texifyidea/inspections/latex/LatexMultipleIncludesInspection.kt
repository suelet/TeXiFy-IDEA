package nl.rubensten.texifyidea.inspections.latex

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import nl.rubensten.texifyidea.insight.InsightGroup
import nl.rubensten.texifyidea.inspections.TexifyInspectionBase
import nl.rubensten.texifyidea.psi.LatexRequiredParam
import nl.rubensten.texifyidea.util.PackageUtils
import nl.rubensten.texifyidea.util.commandsInFile
import nl.rubensten.texifyidea.util.firstChildOfType
import nl.rubensten.texifyidea.util.requiredParameter

/**
 * @author Ruben Schellekens
 */
open class LatexMultipleIncludesInspection : TexifyInspectionBase() {

    override fun getInspectionGroup() = InsightGroup.LATEX

    override fun getInspectionId() = "MultipleIncludes"

    override fun getDisplayName() = "Package has been imported multiple times"

    override fun inspectFile(file: PsiFile, manager: InspectionManager, isOntheFly: Boolean): MutableList<ProblemDescriptor> {
        val descriptors = descriptorList()

        // Find all duplicates.
        val packages = PackageUtils.getIncludedPackagesList(file)
        val covered = HashSet<String>()
        val duplicates = HashSet<String>()
        packages.filterNotTo(duplicates) {
            covered.add(it)
        }

        // Duplicates!
        file.commandsInFile()
                .filter { it.name == "\\usepackage" && it.requiredParameter(0) in duplicates }
                .forEach {
                    val parameter = it.firstChildOfType(LatexRequiredParam::class) ?: error("There must be a required parameter.")
                    descriptors.add(manager.createProblemDescriptor(
                            it,
                            TextRange.from(parameter.textOffset + 1 - it.textOffset, parameter.textLength - 2),
                            "Package has already been included",
                            ProblemHighlightType.GENERIC_ERROR,
                            isOntheFly
                    ))
                }

        return descriptors
    }
}