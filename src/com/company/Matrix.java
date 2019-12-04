package com.company;

class Matrix {
    double[] values;

    Matrix(double[] values) {
        this.values = values;
    }

    Matrix multiply(Matrix other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] +=
                            this.values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new Matrix(result);
    }

    Vertex transform(Vertex in) {
        return new Vertex(
                in.getX() * values[0] + in.getY() * values[4] + in.getZ() * values[8] + in.getW() * values[12],
                in.getX() * values[1] + in.getY() * values[5] + in.getZ() * values[9] + in.getW() * values[13],
                in.getX() * values[2] + in.getY() * values[6] + in.getZ() * values[10] + in.getW() * values[14],
                in.getX() * values[3] + in.getY() * values[7] + in.getZ() * values[11] + in.getW() * values[15]
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                sb.append(values[row * 4 + col]);
                if (col != 3) {
                    sb.append(",");
                }
            }
            if (row != 3) {
                sb.append(";\n ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
/*  //0
                tris.add(new Triangle(new Vertex(0, 0, R, 1),
                        new Vertex(R * sin(alpha) * sin(72 * k), R * sin(alpha) * cos(72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(0), R * sin(alpha) * cos(0), R * cos(alpha), 1),
                       randomColor));
                //1
                tris.add(new Triangle(new Vertex(0, 0, R, 1),
                        new Vertex(R * sin(alpha) * sin(2 * 72 * k), R * sin(alpha) * cos(2 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(72 * k), R * sin(alpha) * cos(72 * k), R * cos(alpha), 1),
                       randomColor));
                //2
                tris.add(new Triangle(new Vertex(0, 0, R, 1),
                        new Vertex(R * sin(alpha) * sin(3 * 72 * k), R * sin(alpha) * cos(3 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(2 * 72 * k), R * sin(alpha) * cos(2 * 72 * k), R * cos(alpha), 1),
                       randomColor));
                //3
                tris.add(new Triangle(new Vertex(0, 0, R, 1),
                        new Vertex(R * sin(alpha) * sin(4 * 72 * k), R * sin(alpha) * cos(4 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(3 * 72 * k), R * sin(alpha) * cos(3 * 72 * k), R * cos(alpha), 1),
                       randomColor));

                //4
                tris.add(new Triangle(new Vertex(0, 0, R, 1),
                        new Vertex(R * sin(alpha) * sin(0), R * sin(alpha) * cos(0), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(4 * 72 * k), R * sin(alpha) * cos(4 * 72 * k), R * cos(alpha), 1),
                       randomColor));

                //5

                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(-36 * k), R * sin(pi - alpha) * cos(-36 * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(0), R * sin(alpha) * cos(0), R * cos(alpha), 1),
                        new Vertex(R * sin(pi - alpha) * sin(36 * k), R * sin(pi - alpha) * cos(36 * k), R * cos(pi - alpha), 1),
                       randomColor));
                //6

                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(36 * k), R * sin(pi - alpha) * cos(36 * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(0), R * sin(alpha) * cos(0), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(72 * k), R * sin(alpha) * cos(72 * k), R * cos(alpha), 1),
                       randomColor));
                //7

                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(36 * k), R * sin(pi - alpha) * cos(36 * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(72 * k), R * sin(alpha) * cos(72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(pi - alpha) * sin(98 * k), R * sin(pi - alpha) * cos((36 + 72) * k), R * cos(pi - alpha), 1),
                       randomColor));
                //8
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(98 * k), R * sin(pi - alpha) * cos((36 + 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(72 * k), R * sin(alpha) * cos(72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(2 * 72 * k), R * sin(alpha) * cos(2 * 72 * k), R * cos(alpha), 1),
                       randomColor));
                //9
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(98 * k), R * sin(pi - alpha) * cos((36 + 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(2 * 72 * k), R * sin(alpha) * cos(2 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(pi - alpha) * sin((36 + 2 * 72) * k), R * sin(pi - alpha) * cos((36 + 2 * 72) * k), R * cos(pi - alpha), 1),
                       randomColor));
                //10
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin((36 + 2 * 72) * k), R * sin(pi - alpha) * cos((36 + 2 * 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(2 * 72 * k), R * sin(alpha) * cos(2 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(3 * 72 * k), R * sin(alpha) * cos(3 * 72 * k), R * cos(alpha), 1),
                       randomColor));
                //11
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin((36 + 2 * 72) * k), R * sin(pi - alpha) * cos((36 + 2 * 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(3 * 72 * k), R * sin(alpha) * cos(3 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(pi - alpha) * sin((36 + 3 * 72) * k), R * sin(pi - alpha) * cos((36 + 3 * 72) * k), R * cos(pi - alpha), 1),
                       randomColor));
                //12
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin((36 + 3 * 72) * k), R * sin(pi - alpha) * cos((36 + 3 * 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(3 * 72 * k), R * sin(alpha) * cos(3 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(4 * 72 * k), R * sin(alpha) * cos(4 * 72 * k), R * cos(alpha), 1),
                       randomColor));
                //13
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin((36 + 3 * 72) * k), R * sin(pi - alpha) * cos((36 + 3 * 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(4 * 72 * k), R * sin(alpha) * cos(4 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(pi - alpha) * sin(-36 * k), R * sin(pi - alpha) * cos(-36 * k), R * cos(pi - alpha), 1),
                       randomColor));
                //14
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(-36 * k), R * sin(pi - alpha) * cos(-36 * k), R * cos(pi - alpha), 1),
                        new Vertex(R * sin(alpha) * sin(4 * 72 * k), R * sin(alpha) * cos(4 * 72 * k), R * cos(alpha), 1),
                        new Vertex(R * sin(alpha) * sin(0), R * sin(alpha) * cos(0), R * cos(alpha), 1),
                       randomColor));
                //15
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(36 * k), R * sin(pi - alpha) * cos(36 * k), R * cos(pi - alpha), 1),
                        new Vertex(0, 0, -R, 1),
                        new Vertex(R * sin(pi - alpha) * sin(-36 * k), R * sin(pi - alpha) * cos(-36 * k), R * cos(pi - alpha), 1),
                       randomColor));
                //16
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(98 * k), R * sin(pi - alpha) * cos((36 + 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(0, 0, -R, 1),
                        new Vertex(R * sin(pi - alpha) * sin(36 * k), R * sin(pi - alpha) * cos(36 * k), R * cos(pi - alpha), 1),
                       randomColor));
                //17
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin((36 + 2 * 72) * k), R * sin(pi - alpha) * cos((36 + 2 * 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(0, 0, -R, 1),
                        new Vertex(R * sin(pi - alpha) * sin(98 * k), R * sin(pi - alpha) * cos((36 + 72) * k), R * cos(pi - alpha), 1),
                       randomColor));
                //18
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin((36 + 3 * 72) * k), R * sin(pi - alpha) * cos((36 + 3 * 72) * k), R * cos(pi - alpha), 1),
                        new Vertex(0, 0, -R, 1),
                        new Vertex(R * sin(pi - alpha) * sin((36 + 2 * 72) * k), R * sin(pi - alpha) * cos((36 + 2 * 72) * k), R * cos(pi - alpha), 1),
                       randomColor));
                //19
                tris.add(new Triangle(new Vertex(R * sin(pi - alpha) * sin(-36 * k), R * sin(pi - alpha) * cos(-36 * k), R * cos(pi - alpha), 1),
                        new Vertex(0, 0, -R, 1),
                        new Vertex(R * sin(pi - alpha) * sin((36 + 3 * 72) * k), R * sin(pi - alpha) * cos((36 + 3 * 72) * k), R * cos(pi - alpha), 1),
                       randomColor));*/

