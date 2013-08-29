package com.umlet.language.sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.Rectangle;
import com.umlet.element.Package;
import com.umlet.language.SortableElement;

public class PackageLayout extends Layout {
	
	private final int ADJUST_TO_PACKAGE_HEAD;

	public PackageLayout() {
		ADJUST_TO_PACKAGE_HEAD = GRIDSIZE*2;
	}
	
	@Override
	public void layout(List<SortableElement> elements) {
		Map<String, List<SortableElement>> packages = extractPackages(elements);
		Map<SortableElement, List<SortableElement>> packList = new TreeMap<SortableElement, List<SortableElement>>();
		
		for(Map.Entry<String, List<SortableElement>> entry: packages.entrySet()) {
			SortableElement pack = createPackageElement(entry.getKey());
			List<SortableElement> packElements = entry.getValue();
			
			Layout l = new HeightLayout();
			l.layout(packElements);

			Dimension size = l.bounds;
			pack.getElement().setSize(size.width, size.height);
			
			packList.put(pack, packElements);
		}
		
		Rectangle x = new Rectangle();
		for (SortableElement pack: packList.keySet()) {
			pack.getElement().setLocation(10, 10 + x.y + x.height);
			x = pack.getElement().getRectangle();
		}
		
		for (SortableElement pack: packList.keySet()) {	
			adjustLocations(pack, packList.get(pack));
			elements.add(pack);
		}
	}

	private void adjustLocations(SortableElement pack, List<SortableElement> packElements) {
		for (SortableElement s: packElements) {
			Rectangle loc = s.getElement().getRectangle();
			Rectangle packLoc = pack.getElement().getRectangle();
			s.getElement().setLocation(loc.x + packLoc.x, loc.y + packLoc.y + ADJUST_TO_PACKAGE_HEAD);
		}
	}

	private Map<String, List<SortableElement>> extractPackages(List<SortableElement> elements) {
		Map<String, List<SortableElement>> packages = new HashMap<String, List<SortableElement>>();
		for (SortableElement element: elements) {
			String packageName = element.getParsedClass().getPackage();
			if (!packages.containsKey(packageName)) {
				packages.put(packageName, new ArrayList<SortableElement>());
			} 
			packages.get(packageName).add(element);	
		}
		return packages;
	}
	
	private SortableElement createPackageElement(String packageName) {
		Package pack = new Package();
		pack.setLocation(10, 10);
		pack.setPanelAttributes(packageName+"\nbg=orange");
		return new SortableElement(pack, packageName);
	}
}
