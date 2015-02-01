/**
 * @author Eloy
 */
var CoolLineRenderer = {

    mainLineColors: {
        //border: "#70707080",
        fill: "#f0f0ff",
        width: 12,
        
        emphasis: {
            fill: "#e0e0ef",
            width: 12
        },
        miniLines: {
            fill: "#e0e0ef",
            width: 2
        }
    },
    
    paintMainLine: function(block, positions, surface, animator){
        var points = [];
        this.getMainLine(block, positions, 0, points);
        
        LinePainter.paintLine(points, surface, this.mainLineColors, true, {
            position: "end",
            length: 15
        }, true, animator);
        
        this.paintLotsOfArrows(points, surface, animator);
    },
    
    paintLotsOfArrows: function(points, surface, animator){
        var sl = 80;
        var al = 20;
        
        for (var i = 1; i < points.length; i++) {
            var p1 = points[i - 1];
            var p2 = points[i];
            
            var dd = {
                dx: p2.x - p1.x,
                dy: p2.y - p1.y
            };
            var l2 = dd.dx * dd.dx + dd.dy * dd.dy;
            if (l2 > sl * sl) {
                var l = Math.sqrt(l2);
                var n = Math.floor(l / 40);
                var sx = dd.dx / n;
                var sy = dd.dy / n;
                var alx = al * sx / sl;
                var aly = al * sy / sl;
                
                for (var j = 1; j < n - 1; j++) {
                    var point = {
                        x: p1.x + sx * (j + .5) - .5 * alx,
                        y: p1.y + sy * (j + .5) - .5 * aly
                    };
                    
                    var line = [point, {
                        x: point.x + alx,
                        y: point.y + aly
                    }];
                    LinePainter.paintLine(line, surface, this.mainLineColors.miniLines, false, {
                        position: "end",
                        length: 4
                    }, true, animator);
                }
            }
        }
    },
    
    getMainLine: function(block, positions, level, points){
        points.push({
            x: positions[level],
            y: points.length == 0 ? 70 : block.getY()
        });
        
        for (var i = 0; i < block.renderClfps.length; i++) {
            var renderClfp = block.renderClfps[i];
            var lastPhase = -10;
            for (var j = 0; j < renderClfp.subBlocks.length; j++) {
                var subBlock = renderClfp.subBlocks[j];
                if (Math.abs(subBlock.parentphase - lastPhase) == 1) {
                    points.pop();
                } else {
                    points.push({
                        x: positions[level],
                        y: renderClfp.position.y + subBlock.desiredPosition.y - LearningFlowRendererOptions.subClfpLineStartHalfGap * renderClfp.scale
                    });
                }
                
                this.getMainLine(subBlock, positions, level + 1, points);
                
                points.push({
                    x: positions[level],
                    y: renderClfp.position.y + subBlock.desiredPosition.y + LearningFlowRendererOptions.subClfpLineStartHalfGap * renderClfp.scale
                });
                
                lastPhase = subBlock.parentphase;
            }
        }
        
        points.push({
            x: positions[level],
            y: block.getBottom() + 20
        });
    }
    
};
