/*
 * SonarQube CSS Plugin
 * Copyright (C) 2013-2016 Tamas Kende and David RACODON
 * mailto: kende.tamas@gmail.com and david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.css.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.css.model.pseudo.pseudofunction.UnknownPseudoFunction;
import org.sonar.css.model.pseudo.pseudoidentifier.UnknownPseudoIdentifier;
import org.sonar.plugins.css.api.tree.PseudoFunctionTree;
import org.sonar.plugins.css.api.tree.PseudoIdentifierTree;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "unknown-pseudo",
  name = "Unknown pseudo-elements and pseudo-classes should be removed",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class UnknownPseudoCheck extends DoubleDispatchVisitorCheck {

  @Override
  public void visitPseudoFunction(PseudoFunctionTree tree) {
    if (tree.standardFunction() instanceof UnknownPseudoFunction && !tree.isVendorPrefixed()) {
      createIssue(tree.function(), tree.standardFunction().getName());
    }
    super.visitPseudoFunction(tree);
  }

  @Override
  public void visitPseudoIdentifier(PseudoIdentifierTree tree) {
    if (tree.standardPseudoIdentifier() instanceof UnknownPseudoIdentifier && !tree.isVendorPrefixed()) {
      createIssue(tree.identifier(), tree.standardPseudoIdentifier().getName());
    }
    super.visitPseudoIdentifier(tree);
  }

  private void createIssue(Tree location, String pseudo) {
    addPreciseIssue(location, "Remove this usage of the unknown \"" + pseudo + "\" pseudo.");
  }

}
