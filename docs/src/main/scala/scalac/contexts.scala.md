
```scala
package ohnosequences.scalac

import scala.tools.nsc
import nsc.Global
import nsc.plugins.{ Plugin, PluginComponent }
import nsc.transform.Transform
import nsc.symtab.Flags._
import nsc.ast.TreeDSL

final
class ContextsPlugin(val global: Global) extends Plugin {
  val name        = "contexts"
  val description = "Provides syntax for contexts `x ⊢ { ... }`"
  val components  = new AddValsAndImport(this, global) :: Nil
}

final
class AddValsAndImport(plugin: Plugin, val global: Global) extends PluginComponent with Transform with TreeDSL {

  import global._

  val runsAfter =
    "parser" :: Nil
  val phaseName =
    "local-imports"

  val ⊢ : String =
    "$u22A2"

  final
  def newTransformer(unit: CompilationUnit): Transformer =
    new Transformer() {

      override
      def transform(tree: Tree): Tree =
        tree match {

          case Apply(Select(valValue, TermName(⊢)), blocks) =>
            blocks.headOption
              .fold(super.transform(tree)) {
                block => super.transform(addValsAndImportTo(List(valValue), block))
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

```




[test/scala/scalac/contextsTest.scala]: ../../../test/scala/scalac/contextsTest.scala.md
[main/scala/scalac/contexts.scala]: contexts.scala.md