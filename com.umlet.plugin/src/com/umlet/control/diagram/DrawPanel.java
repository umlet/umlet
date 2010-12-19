package com.umlet.control.diagram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.help.StartUpHelpLabel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Printable {
    private JScrollPane _scr;
    private int _incx;
    private int _incy;
    private Selector selector;
    private DiagramHandler handler;
    
    public DrawPanel(DiagramHandler handler) {
    	this._incx = 0;
    	this._incy = 0;
    	this.handler = handler;
    	this.setLayout(null);
    	this.setBackground(Color.white);
    	this.selector = new Selector(this);
    	JScrollPane p = new JScrollPane() {
			@Override
			public void setEnabled(boolean en) {
				super.setEnabled(en);
				this.getViewport().getView().setEnabled(en);
			}
    		
    	};
    	p.getHorizontalScrollBar().setUnitIncrement(20);
    	p.getVerticalScrollBar().setUnitIncrement(20);
    	p.setBorder(null);
    	this.setScrollPanel(p);
    }
    
    @Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		this.handler.setEnabled(en);
		for(Component c : this.getComponents())
			c.setEnabled(en);
		if(en)
			this.setBackground(new Color(255,255,255));
		else
			this.setBackground(new Color(235,235,235));
	}

	public void setStartUpHelpLabel(StartUpHelpLabel label) {
    	this.addContainerListener(label);
    	this.add(label);
    }
    
    public DiagramHandler getHandler() {
    	return this.handler;
    }
    
    public Dimension getPreferredSize() {
              int retx=0;
              int rety=0;
              for (int i=0; i<getComponents().length; i++) {
                 Component c=getComponent(i);
                 int x=c.getX()+c.getWidth();
                 int y=c.getY()+c.getHeight();
                 if (x>retx) retx=x;
                 if (y>rety) rety=y;
              }
              if (_incx>0) {
                  int viewsx = _scr.getViewport().getWidth();
                  if (viewsx+_incx>retx) retx=viewsx+_incx;
              }
              if (_incy>0) {
                  int viewsy = _scr.getViewport().getHeight();
                  if (viewsy+_incy>rety) rety=viewsy+_incy;
              }
              return new Dimension(retx, rety);
    }
    
    private void setScrollPanel(JScrollPane scr) {
       _scr=scr;
       scr.setViewportView(this);
    }
    
    public JScrollPane getScrollPanel() {
         return _scr;
    }
    
    public void incViewPosition(int incx, int incy) {
       _incx+=incx;
       _incy+=incy;
       Point viewp=_scr.getViewport().getViewPosition();
       _scr.getViewport().setViewSize(getPreferredSize());
       _scr.getViewport().setViewPosition(new Point(viewp.x+incx, viewp.y+incy));
    }
    
    public Rectangle getContentBounds() {
		int minx=Integer.MAX_VALUE;
		int miny=Integer.MAX_VALUE;
		int maxx=0;
		int maxy=0;
    	
		Vector<Entity> v = handler.getDrawPanel().getAllEntities();
		for (int i=0; i<v.size(); i++) {
			Entity e = v.elementAt(i);
			minx=Math.min(minx,e.getX()-Constants.printpadding);
			miny=Math.min(miny,e.getY()-Constants.printpadding);
			maxx=Math.max(maxx,e.getX()+e.getWidth()+Constants.printpadding);
			maxy=Math.max(maxy,e.getY()+e.getHeight()+Constants.printpadding);
		}
		return new Rectangle(minx,miny,maxx-minx,maxy-miny);
    }
    
    public void paintEntitiesIntoGraphics2D(Graphics2D g2d) {
	    Vector<Entity> v=handler.getDrawPanel().getAllEntities();

        for (int i=v.size()-1; i>=0; i--) {
        	Entity e= v.elementAt(i);
            g2d.translate(e.getX(), e.getY());
            e.paint(g2d);
            g2d.translate(-e.getX(), -e.getY());
        }
    }
    
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) 
	{
		if (pageIndex > 0)
		      return(NO_SUCH_PAGE);
		else 
		{
		      Graphics2D g2d = (Graphics2D)g;
		      RepaintManager currentManager = RepaintManager.currentManager(this);
		      currentManager.setDoubleBufferingEnabled(false);
		      Rectangle bounds = this.getContentBounds();
		      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		      AffineTransform t = g2d.getTransform();
		      double scale = Math.min(pageFormat.getImageableWidth() / bounds.width, 
		    		  pageFormat.getImageableHeight() / bounds.height);
		      if(scale < 1)
		      {
		    	  t.scale(scale,scale);
		    	  g2d.setTransform(t);
		      }
		      g2d.translate(-bounds.x, -bounds.y);
		      this.paint(g2d);
		      currentManager = RepaintManager.currentManager(this);
		      currentManager.setDoubleBufferingEnabled(true);
		      return(PAGE_EXISTS);
		}
	}
    
    public Vector<Entity> getAllEntities() {
        Vector<Entity> v=new Vector<Entity>();
        for (int i=0; i<this.getComponentCount(); i++) {
          Component c = this.getComponent(i);
          if(c instanceof Entity)
        	  v.add((Entity)c);
        }
        return v;
    }
    
    public Vector<Entity> getNotInGroupEntitiesOnPanel() {
		Vector<Entity> all = this.getAllEntities();
		Vector<Entity> entities = new Vector<Entity>();
		for(Entity e : all) 
		{
			if(!e.isPartOfGroup())
				entities.add(e);
		}
		return entities;
    }
    
    public Vector<Relation> getAllRelationsOnPanel() {
        Component[] tmp=this.getComponents();
        Vector<Relation> ret=new Vector<Relation>();
        for (int i=0; i<tmp.length; i++) {
          if (tmp[i] instanceof Relation)
            ret.add((Relation)tmp[i]);
        }
        return ret;
    }
    
	public Selector getSelector() {
		return this.selector;
	}
    
    public void realignToGrid() { //LME
  	  Vector<Entity> v= this.getAllEntities();
  	  for(int i=0;i<v.size();i++) {
  		  Entity entity = v.elementAt(i);
  		  int x=entity.getX();
  		  int y=entity.getY();
  		  if(entity instanceof Relation) {
  			  Relation r = (Relation)entity;
  			  r.reLocate();
  		} else {
  			entity.setLocation(x-(x%Umlet.getInstance().getMainUnit()),y-(y%Umlet.getInstance().getMainUnit()));
  		}
  	  }
    }

	@Override
	protected void paintChildren(Graphics g) {
		//TODO to implement zoom move commands... have to be altered too
		/*Graphics2D g2 = (Graphics2D)g;
		AffineTransform t = g2.getTransform();
		float scale = this.getHandler().getZoom();
		t.scale(scale, scale);
		g2.setTransform(t);*/
		super.paintComponents(g);
	}    
    
    
}