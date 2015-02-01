/**
 * @author Eloy
 */
var AssessmentLineRenderer = {

    thickAssessmentLineColors : {
        "assessed" : {
            base : function() {
                return AssessmentLineRenderer.assessmentLineColors["assessed"];
            },
            width : 15,
            borderWidth : 3
        },
        "summative" : {
            base : function() {
                return AssessmentLineRenderer.assessmentLineColors["summative"];
            },
            width : 5
        },
        "formative" : {
            base : function() {
                return AssessmentLineRenderer.assessmentLineColors["formative"];
            },
            width : 5
        },
        "diagnosis" : {
            base : function() {
                return AssessmentLineRenderer.assessmentLineColors["diagnosis"];
            },
            width : 5
        }
    },
    assessmentLineColors : {
        "assessed" : {
            border : "#707070",
            fill : "#ffa880",
            width : 6,
            emphasis : {
                border : "#303030",
                fill : "#ffcaa2"
            }
        },
        "summative" : {
            border : "#707070",
            fill : "#ffa840",
            width : 5,
            emphasis : {
                border : "#303030",
                fill : "#ffca62"
            }
        },
        "formative" : {
            border : "#707070",
            fill : "#f0a8a8",
            width : 5,

            emphasis : {
                border : "#303030",
                fill : "#f2caca"
            }
        },
        "diagnosis" : {
            border : "#707070",
            fill : "#f0d880",
            width : 5,
            emphasis : {
                border : "#303030",
                fill : "#f2faa2"
            }
        }
    },

    positionsUsed : null,

    reset : function() {
        this.positionsUsed = {};
    },
    paintLines : function(assessment, origin, renderer, verticalLines) {

        var fromLevel = origin.clfp ? renderer.renderData.mainBlock.getRenderClfp(origin.clfp.id).level : -1;
        this.paintLine(assessment, origin, fromLevel, assessment.getAssessedActivity(), "assessed", this.assessmentLineColors["assessed"], "start", renderer, verticalLines);

        var functions = assessment.getFunctions();
        for(var i = 0; i < functions.length; i++) {
            if(functions[i].subtype != "summative") {
                var activity = IDPool.getObject(functions[i].link);
                if(activity) {
                    this.paintLine(assessment, origin, fromLevel, activity, functions[i].subtype, this.assessmentLineColors[functions[i].subtype], "end", renderer, verticalLines);
                }
            }
        }
    },
    paintLine : function(assessment, origin, fromLevel, finalActivity, lineType, lineColors, arrowPosition, renderer, verticalLines) {
        var finalLocation = AssessmentFlowRenderer.getActivityLocation(finalActivity);
        if(finalLocation) {
            var finalRenderClfp = renderer.renderData.mainBlock.getRenderClfp(finalLocation.clfp.id);

            if(fromLevel < 0) {
                var toTheLeft = Math.abs(verticalLines[finalRenderClfp.level] - origin.point.x) < Math.abs(verticalLines[finalRenderClfp.level + 1] - origin.point.x);
            } else {
                toTheLeft = fromLevel < finalRenderClfp.level;
            }

            //var finalPoint = AssessmentFlowRenderer.positionToPoint(finalLocation, renderer.renderData.mainBlock, toTheLeft);
            var finalPoint = AssessmentFlowRenderer.positionToPoint(finalLocation, renderer.renderData.mainBlock, toTheLeft, renderer.uiListener);
            var posKey = finalPoint.x + "_" + finalPoint.y;

            if( posKey in this.positionsUsed) {
                this.positionsUsed[posKey]++;
            } else {
                this.positionsUsed[posKey] = 0;
            }
            finalPoint.x += (this.positionsUsed[posKey] + 1) * 13;
            finalPoint.y += this.positionsUsed[posKey] * 8;

            var iconFinalPoint = AssessmentIcons.getData(lineType, 25, 1, finalPoint);
            finalPoint.width = iconFinalPoint.width;
            finalPoint.height = iconFinalPoint.height;
            var iconShape = renderer.bigGroup.createImage(iconFinalPoint);
            renderer.animator.addNoAnimStuff(iconShape);
            AssessmentEmphasisManager.registerImage(assessment, iconShape, iconFinalPoint);
            renderer.uiListener.registerActiveElement(iconShape, {
                id : assessment.id,
                line : lineType
            });

            var key = null;
            switch (linetype) {
                case "assessment":
                    key = "help.assessment.icon.assessment";
                    break;
                case "assessed":
                    key = "help.assessment.icon.assessed";
                    break;
                case "diagnosis":
                    key = "help.assessment.icon.diagnosis";
                    break;
                case "formative":
                    key = "help.assessment.icon.formative";
                    break;
            }

            var tooltip = i18n.getReplaced1(key, assessment.getTitle());
            GFXTooltip.register(iconShape, tooltip);

            var pointDefs = this.createCoolAssessmentLineDefs(fromLevel, origin.point.y, finalRenderClfp.level, finalPoint.y, renderer.renderData.separators, toTheLeft);
            var line = this.createCoolAssessmentLine(origin.point, fromLevel, finalPoint, pointDefs, verticalLines);

            var shape = LinePainter.paintLine(line, renderer.bigGroup, lineColors, true, {
                position : arrowPosition
            }, true, renderer.animator, true);

            var strokes = LinePainter.getStrokes(lineColors);
            AssessmentEmphasisManager.registerShape(assessment, shape, strokes);

            renderer.uiListener.registerActiveElement(shape, {
                id : assessment.id,
                line : lineType
            }, true);
        }
    },
    createCoolAssessmentLine : function(originPoint, fromLevel, finalPoint, pointDefs, verticalLines) {
        var points = [];
        points.push({
            x : originPoint.x,
            y : originPoint.y
        });

        if(fromLevel < 0) {
            var lastLevel = -1;
            //pointDefs[0].level + pointDefs[0].position;
            var lastSeparator = pointDefs[0].y;

            var last = {
                x : originPoint.x,
                y : lastSeparator + 15 * (Math.random() - .5)
            };
            points.push({
                x : last.x,
                y : last.y
            });

        } else {
            lastLevel = fromLevel + 1;
            lastSeparator = originPoint.y;
            last = {
                x : originPoint.x,
                y : originPoint.y
            };
        }

        for(var i = 0; i < pointDefs.length; i++) {
            var newLevel = pointDefs[i].level + pointDefs[i].position;
            var point = {
                x : 0,
                y : 0
            };

            if(lastSeparator == pointDefs[i].y) {
                point.keepY = true;
                point.y = last.y;
                point.x = verticalLines[newLevel] + 15 * (Math.random() - .5);
            } else {
                point.keepX = newLevel == lastLevel;
                point.y = pointDefs[i].y + 15 * (Math.random() - .5);
                point.x = point.keepX ? last.x : verticalLines[newLevel] + 15 * (Math.random() - .5);
            }

            /*            var point = {
             y: lastSeparator == pointDefs[i].y ? last.y : pointDefs[i].y + 15 * (Math.random() - .5),
             x: newLevel == lastLevel ? last.x : verticalLines[newLevel] + 15 * (Math.random() - .5)
             };
             */
            points.push(point);
            lastLevel = newLevel;
            lastSeparator = pointDefs[i].y;
            last.y = point.y;
            last.x = point.x;
        }

        points.push({
            x : finalPoint.x,
            y : last.y
        });
        points.push(finalPoint);

        points[0].width = points[0].height = 40;

        Intersection.intersect(points);

        return points;
    },
    createCoolAssessmentLineDefs : function(fromLevel, fromY, toLevel, toY, separators, toTheLeft) {
        var pointDefs = [];
        /* pointDefs.push({
         level: fromLevel,
         position: 1,
         y: fromY
         });*/
        if(fromLevel < 0) {
            pointDefs.push({
                level : toLevel,
                position : toTheLeft ? 0 : 1,
                y : 80
            });

            pointDefs.push({
                level : toLevel,
                position : toTheLeft ? 0 : 1,
                y : toY
            });
        } else if(fromLevel == toLevel) {
            pointDefs.push({
                level : fromLevel,
                position : 1,
                y : fromY
            });
            pointDefs.push({
                level : toLevel,
                position : 1,
                y : toY
            });
        } else if(toLevel == fromLevel + 1) {
            pointDefs.push({
                level : toLevel,
                position : 0,
                y : toY
            });
        } else if(toLevel > fromLevel + 1) {
            for(var i = fromLevel + 1; i < toLevel; i++) {
                var possibleSeparators = [];
                for(var j = 0; j < separators[i].length; j++) {
                    var separator = separators[i][j];
                    if((fromY - separator) * (toY - separator) <= 0) {
                        possibleSeparators.push(separator);
                    }
                }

                if(possibleSeparators.length == 0) {
                    possibleSeparators = separators[i];
                }

                var min = 10000000000;
                var best = 0;

                for( j = 0; j < possibleSeparators.length; j++) {
                    var d1 = possibleSeparators[j] - fromY;
                    var d2 = possibleSeparators[j] - toY;
                    var err = d1 * d1 + d2 * d2;
                    if(err < min) {
                        best = j;
                        min = err;
                    }
                }
                fromY = possibleSeparators[best];

                pointDefs.push({
                    level : i,
                    position : 0,
                    y : fromY
                });

                pointDefs.push({
                    level : i + 1,
                    position : 0,
                    y : fromY
                });

            }
            pointDefs.push({
                level : toLevel,
                position : 0,
                y : toY
            });
        } else/*if (toLevel < fromLevel)*/
        {
            for( i = fromLevel; i > toLevel; i--) {
                possibleSeparators = [];
                for( j = 0; j < separators[i].length; j++) {
                    separator = separators[i][j];
                    if((fromY - separator) * (toY - separator) <= 0) {
                        possibleSeparators.push(separator);
                    }
                }

                if(possibleSeparators.length == 0) {
                    possibleSeparators = separators[i];
                }
                min = 10000000000;
                best = 0;

                for( j = 0; j < possibleSeparators.length; j++) {
                    d1 = possibleSeparators[j] - fromY;
                    d2 = possibleSeparators[j] - toY;
                    err = d1 * d1 + d2 * d2;
                    if(err < min) {
                        best = j;
                        min = err;
                    }
                }
                fromY = possibleSeparators[best];

                pointDefs.push({
                    level : i,
                    position : 1,
                    y : fromY
                });
                pointDefs.push({
                    level : i - 1,
                    position : 1,
                    y : fromY
                });

            }
            pointDefs.push({
                level : toLevel,
                position : 1,
                y : toY
            });
        }

        return pointDefs;
    }
};
