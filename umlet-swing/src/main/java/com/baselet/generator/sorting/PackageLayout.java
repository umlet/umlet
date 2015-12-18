package com.baselet.generator.sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.ElementId;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.NewGridElement;

public class PackageLayout extends Layout {

	private final int ADJUST_TO_PACKAGE_HEAD;

	public PackageLayout() {
		ADJUST_TO_PACKAGE_HEAD = GRIDSIZE * 2;
	}

	@Override
	public void layout(List<SortableElement> elements) {
		Map<String, List<SortableElement>> packages = extractPackages(elements);
		Map<SortableElement, List<SortableElement>> packList = new TreeMap<SortableElement, List<SortableElement>>();

		for (Map.Entry<String, List<SortableElement>> entry : packages.entrySet()) {
			SortableElement pack = createPackageElement(entry.getKey());
			List<SortableElement> packElements = entry.getValue();

			Layout l = new HeightLayout();
			l.layout(packElements);

			Dimension size = l.bounds;
			pack.getElement().setSize(size.width, size.height);

			packList.put(pack, packElements);
		}

		Rectangle x = new Rectangle();
		for (SortableElement pack : packList.keySet()) {
			pack.getElement().setLocation(10, 10 + x.y + x.height);
			x = pack.getElement().getRectangle();
		}

		for (Entry<SortableElement, List<SortableElement>> entry : packList.entrySet()) {
			adjustLocations(entry.getKey(), entry.getValue());
			elements.add(entry.getKey());
		}
	}

	private void adjustLocations(SortableElement pack, List<SortableElement> packElements) {
		for (SortableElement s : packElements) {
			Rectangle loc = s.getElement().getRectangle();
			Rectangle packLoc = pack.getElement().getRectangle();
			s.getElement().setLocation(loc.x + packLoc.x, loc.y + packLoc.y + ADJUST_TO_PACKAGE_HEAD);
		}
	}

	private Map<String, List<SortableElement>> extractPackages(List<SortableElement> elements) {
		Map<String, List<SortableElement>> packages = new HashMap<String, List<SortableElement>>();
		for (SortableElement element : elements) {
			String packageName = element.getParsedClass().getPackage();
			if (!packages.containsKey(packageName)) {
				packages.put(packageName, new ArrayList<SortableElement>());
			}
			packages.get(packageName).add(element);
		}
		return packages;
	}

	private SortableElement createPackageElement(String packageName) {
		NewGridElement pack = ElementFactorySwing.create(ElementId.UMLPackage, new Rectangle(10, 10, 10, 10), packageName + "\nbg=orange", "", CurrentDiagram.getInstance().getDiagramHandler());
		return new SortableElement(pack, packageName);
	}
}
