package ohnosequences.scalac

import scala.tools.nsc
import nsc.Global
import nsc.plugins.{ Plugin, PluginComponent }
import nsc.transform.Transform
import nsc.symtab.Flags._
import nsc.ast.TreeDSL

final
class LocalImportsPlugin(val global: Global) extends Plugin {
  val name        = "local-import"
  val description = "Provides syntax for automatic local val and import"
  val components  = new AddValsAndImport(this, global) :: Nil
}

final
class AddValsAndImport(plugin: Plugin, val global: Global) extends PluginComponent with Transform with TreeDSL {

  import global._

  val runsAfter =
    "parser" :: Nil
  val phaseName =
    "local-imports"
  val insideName =
    TermName("inside")

  final
  def newTransformer(unit: CompilationUnit): Transformer =
    new Transformer() {

      override
      def transform(tree: Tree): Tree =
        tree match {

          case Apply(Apply(Ident(insideName), valsValues), blocks) =>
            blocks.headOption
              .fold(super.transform(tree)){
                block => super.transform(addValsAndImportTo(valsValues, block))
              }

          case other =>
            super.transform(other)
        }
  }

  private
  def addValsAndImportTo(valsValues: List[Tree], block: Tree): Tree = {

    val vals: List[ValDef] =
      valsValues.zipWithIndex map {
        case (imp, idx) =>
          ValDef(
            Modifiers(FINAL),
            TermName("inside_local_" + idx),
            TypeTree(),
            imp
          )
      }

    val imports: List[Tree] =
      vals map { imp => Import(Ident(imp.name), ImportSelector.wildList) }

    Block((vals: List[Tree]) ++ imports, block)
  }
}
