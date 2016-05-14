function [ inliers, M ] = distfn( M, x, t )
%DISTFN Summary of this function goes here
%   Detailed explanation goes here
    
    cols = size(x, 2);
    x_pad = [x(1:2, 1:cols); ones(1, cols); x(3:4, 1:cols); ones(1, cols)];
    
    inliers = [];
    for i = 1 : cols
        v_top = x_pad(1:3, i);
        v_top = M * v_top;
        v_top_norm = v_top / v_top(3); % 3rd coordinate will be 1.
        v_bottom = x_pad(4:6, i);
        
        distance = v_top_norm - v_bottom;
        if (norm(distance) < t)
            inliers = [inliers i];
        end
    end
    
end

