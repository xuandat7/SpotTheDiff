
import java.awt.Point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author xuandat7
 */
import java.awt.*;

public class DifferenceRegion {
    int x, y, size;

    public DifferenceRegion(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    // Kiểm tra xem điểm click có nằm trong vùng khác biệt không
    public boolean contains(Point p) {
        return (p.x >= x && p.x <= x + size && p.y >= y && p.y <= y + size);
    }

    // Vẽ vùng khác biệt (khi chưa tìm thấy)
    public void draw(Graphics g, int offsetX) {
        g.setColor(Color.RED);
        g.drawRect(x + offsetX, y, size, size);  // Thêm offsetX để vẽ bên phải
    }
}
